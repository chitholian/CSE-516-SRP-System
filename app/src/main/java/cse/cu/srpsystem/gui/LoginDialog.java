package cse.cu.srpsystem.gui;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.List;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.data.DataModel;
import cse.cu.srpsystem.data.DataReceiver;
import cse.cu.srpsystem.data.LocalDataHandler;
import cse.cu.srpsystem.data.RemoteDataHandler;
import cse.cu.srpsystem.entities.User;
import cse.cu.srpsystem.logics.StatusListener;
import cse.cu.srpsystem.logics.ThreadLogics;

public class LoginDialog extends DialogFragment {
    private View layout;
    private EditText emailField, passwordField;
    private Button loginButton, exitButton;
    private Spinner roleSpinner;
    private ProgressBar progressBar;
    private ThreadLogics.CustomThread loginThread, dataRefresherThread;
    private Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        layout = LayoutInflater.from(context).inflate(R.layout.activity_login, null);
        emailField = layout.findViewById(R.id.field_email);
        passwordField = layout.findViewById(R.id.field_password);
        progressBar = layout.findViewById(R.id.progress_bar);
        (roleSpinner = layout.findViewById(R.id.spinner_roles)).setAdapter(new ArrayAdapter<>(
                context, R.layout.sample_text, new String[]{"Exam Controller", "Teacher", "Student"}
        ));
        (exitButton = layout.findViewById(R.id.button_exit_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginThread != null)
                    loginThread.cancel(true);
                dismiss();
            }
        });
        (loginButton = layout.findViewById(R.id.button_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);

                (loginThread = new ThreadLogics.CustomThread(new ThreadLogics.CustomThread.Runnable() {
                    @Override
                    public StatusListener.Status run(Object... parameters) {
                        return new User(0, null).login(emailField.getText().toString(), roleSpinner.getSelectedItem().toString(), passwordField.getText().toString());
                    }
                }, new StatusListener() {
                    @Override
                    public void listen(Status status) {
                        switch (status) {
                            case SUCCESSFUL:
                                UITools.openDashboard(context, false);
                                ((Activity) context).finish();
                                return;
                            case ERR_INCORRECT_CREDENTIAL:
                                UITools.showMessage(context, "Error! Incorrect credentials.");
                                break;

                            case ERR_CONNECTION_FAILED:
                                UITools.showMessage(context, "Error! Connection failed.");
                                break;

                            default:
                                UITools.showMessage(context, "Oops! Unknown error occurred.");
                        }
                        v.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                })).execute();
            }
        });
        /*//todo:remove it;
        emailField.setText("chitholian@gmail.com");
        passwordField.setText("123456");
        //roleSpinner.setSelection(2);
        //loginButton.performClick();*/
        return new AlertDialog.Builder(context).setView(layout).setTitle("SRPS Login").create();
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (loginThread != null) loginThread.cancel(true);
    }
}
