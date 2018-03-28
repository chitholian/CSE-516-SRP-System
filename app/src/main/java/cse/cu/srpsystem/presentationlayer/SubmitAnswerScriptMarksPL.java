package cse.cu.srpsystem.presentationlayer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.applicationlayer.StatusListener;
import cse.cu.srpsystem.applicationlayer.SubmitMarksBL;
import cse.cu.srpsystem.applicationlayer.ThreadRunnerBL;

public class SubmitAnswerScriptMarksPL extends DialogFragment {
    private View layout;
    private Context context;
    private Button addButton, doneButton;
    private AutoCompleteTextView codeView;
    private EditText marksField;
    private ProgressBar progressBar;
    private ArrayAdapter<Integer> autocompleteAdapter;
    private SubmitMarksBL logic;
    private int exam_id;
    private int code;
    private ThreadRunnerBL.CustomThread submissionThread;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        layout = LayoutInflater.from(context).inflate(R.layout.submit_script_marks, null);
        (codeView = layout.findViewById(R.id.code)).setAdapter((autocompleteAdapter = new ArrayAdapter<>(context, R.layout.sample_text,
                (logic = new SubmitMarksBL(exam_id)).getScriptCodes(context))));
        marksField = layout.findViewById(R.id.marks);
        (addButton = layout.findViewById(R.id.add_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                if (codeView.getText().length() == 0 || marksField.getText().length() == 0) {
                    UITools.showMessage(context, "You must provide script code as well as marks.");
                    v.setEnabled(true);
                    return;
                }
                float marks = Float.parseFloat(marksField.getText().toString());
                if (marks < 0 || marks > 26.25) {
                    UITools.showMessage(context, "Sorry! Script marks ranges from 0.0 to 26.25");
                    v.setEnabled(true);
                    return;
                }
                if (logic.addMarks(context, code = Integer.parseInt(codeView.getText().toString()), marks)) {
                    UITools.showToast(context, "Marks Added/Updated");
                    autocompleteAdapter.remove(code);
                    codeView.setText(null);
                    marksField.setText(null);
                } else UITools.showMessage(context, "Error! Script code did not match.\n" +
                        "Please refresh local data and try again.");
                v.setEnabled(true);
            }
        });
        progressBar = layout.findViewById(R.id.progress);
        (doneButton = layout.findViewById(R.id.done_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITools.confirm(context, "Are you sure to submit ?", new UITools.ConfirmListener() {
                    @Override
                    public void ifYes() {
                        addButton.setEnabled(false);
                        doneButton.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        setCancelable(false);
                        (submissionThread = new ThreadRunnerBL.CustomThread(new ThreadRunnerBL.CustomThread.Runnable() {
                            @Override
                            public StatusListener.Status run(Object... parameters) {
                                return logic.submitAll(context);
                            }
                        }, new StatusListener() {
                            @Override
                            public void listen(Status status) {
                                switch (status) {
                                    case SUCCESSFUL:
                                        UITools.showMessage(context, "Operation Successful.");
                                        dismiss();
                                        return;
                                    case ERR_CONNECTION_FAILED:
                                        UITools.showMessage(context, "Error! Cannot connect to server.");
                                        break;
                                    case ERR_PERMISSION_DENIED:
                                        UITools.showMessage(context, "Error! Server rejected your submission.");
                                        break;
                                    case UNKNOWN_ERROR:
                                    default:
                                        UITools.showMessage(context, "Oops! Unknown error.");

                                }
                                addButton.setEnabled(true);
                                doneButton.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                setCancelable(true);
                            }
                        })).execute();
                    }

                    @Override
                    public void ifNo() {

                    }
                });
            }
        });

        return new AlertDialog.Builder(context).setView(layout).setTitle("Submit Script Marks").create();
    }

    public SubmitAnswerScriptMarksPL setExam(int exam_id) {
        this.exam_id = exam_id;
        return this;
    }

    @Override
    public void onDestroy() {
        if (submissionThread != null) submissionThread.cancel(true);
        super.onDestroy();
    }
}
