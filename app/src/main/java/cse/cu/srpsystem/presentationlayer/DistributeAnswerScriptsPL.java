package cse.cu.srpsystem.presentationlayer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.dataaccesslayer.LocalDAL;
import cse.cu.srpsystem.entities.Course;
import cse.cu.srpsystem.entities.Teacher;
import cse.cu.srpsystem.applicationlayer.DistributeAnswerScriptsBL;
import cse.cu.srpsystem.applicationlayer.StatusListener;
import cse.cu.srpsystem.applicationlayer.ThreadRunnerBL;

public class DistributeAnswerScriptsPL extends DialogFragment {
    private Context context;
    private View layout;
    private Spinner examiners, courses, parts;
    private AutoCompleteTextView studentID;
    private Button generateButton, addButton, doneButton;
    private ProgressBar progressBar;
    private TextView scriptCodeView;
    private DistributeAnswerScriptsBL logic;
    private ThreadRunnerBL.CustomThread submissionThread;
    private ArrayAdapter<Integer> autoCompleteAdapter;

    public DistributeAnswerScriptsPL setExam(int exam_id) {
        logic = new DistributeAnswerScriptsBL(exam_id);
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        layout = LayoutInflater.from(context).inflate(R.layout.activity_distribution, null);
        (examiners = layout.findViewById(R.id.spinner_examiner)).setAdapter(new ArrayAdapter<>(context, R.layout.sample_text, LocalDAL.getInstance(context).getTeachers()));

        if (examiners.getSelectedItem() == null)
            return new AlertDialog.Builder(context).setMessage("Error! No teacher found.\n" +
                    "Please refresh local data and try again.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            }).create();
        (courses = layout.findViewById(R.id.spinner_courses)).setAdapter(new ArrayAdapter<>(context, R.layout.sample_text,
                LocalDAL.getInstance(context).getTheoryCoursesByExam(logic.getExamID())));
        if (courses.getSelectedItem() == null)
            return new AlertDialog.Builder(context).setMessage("Error! No course found.\n" +
                    "Please refresh local data and try again.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            }).create();

        (studentID = layout.findViewById(R.id.student_id)).setAdapter(autoCompleteAdapter = new ArrayAdapter<>(
                context, R.layout.sample_text, LocalDAL.getInstance(context).getStudentIDsByExam(logic.getExamID())
        ));
        if (autoCompleteAdapter.getCount() == 0)
            return new AlertDialog.Builder(context).setMessage("Error! No student found.\n" +
                    "Please refresh local data and try again.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            }).create();

        (parts = layout.findViewById(R.id.spinner_parts)).setAdapter(new ArrayAdapter<>(context, R.layout.sample_text,
                new Character[]{'A', 'B'}));


        progressBar = layout.findViewById(R.id.progress_bar);
        (scriptCodeView = layout.findViewById(R.id.view_script_code)).setText("Code: " + logic.generateCode(context));
        (addButton = layout.findViewById(R.id.button_assign_code)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                if (examiners.getSelectedItem() == null) {
                    UITools.showMessage(context, "You must select an examiner.");
                    v.setEnabled(true);
                    return;
                }
                String student_id = studentID.getText().toString();
                if (student_id.isEmpty()) {
                    UITools.showMessage(context, "Please insert student ID.");
                } else if (logic.assignCode(context, ((Teacher) examiners.getSelectedItem()).id, Integer.parseInt(student_id),
                        ((Course) courses.getSelectedItem()).code, (char) parts.getSelectedItem())) {
                    UITools.showToast(context, "Assignment OK");
                    try {
                        autoCompleteAdapter.remove(Integer.parseInt(studentID.getText().toString()));
                        //studentID.setText(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    generateButton.performClick();
                } else UITools.showMessage(context, "Error! Assignment failed.");
                v.setEnabled(true);
            }
        });
        (generateButton = layout.findViewById(R.id.button_generate_code)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scriptCodeView.setText("Code: " + logic.generateCode(context));

                addButton.setEnabled(true);
            }
        });
        (doneButton = layout.findViewById(R.id.button_assign_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITools.confirm(context, "Are you sure to submit ?", new UITools.ConfirmListener() {
                    @Override
                    public void ifYes() {
                        addButton.setEnabled(false);
                        generateButton.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        doneButton.setEnabled(false);
                        doneButton.setText("Sending...");
                        setCancelable(false);

                        (submissionThread = new ThreadRunnerBL.CustomThread(new ThreadRunnerBL.CustomThread.Runnable() {
                            @Override
                            public StatusListener.Status run(Object... parameters) {
                                return logic.submitAll(context);
                            }
                        }, new StatusListener() {
                            @Override
                            public void listen(Status status) {
                                if (status.equals(Status.SUCCESSFUL))
                                    UITools.showMessage(context, "Operation Successful.");
                                else if (status.equals(Status.ERR_CONNECTION_FAILED))
                                    UITools.showMessage(context, "Error! Connection failed.");
                                else
                                    UITools.showMessage(context, "Oops! Unknown error.");
                                dismiss();
                                addButton.setEnabled(true);
                                generateButton.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                doneButton.setEnabled(true);
                                doneButton.setText("Done");
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

        return new AlertDialog.Builder(context).setView(layout).setTitle("Script Distribution").create();
    }

    @Override
    public void onDestroy() {
        if (submissionThread != null)
            submissionThread.cancel(true);
        super.onDestroy();
    }
}
