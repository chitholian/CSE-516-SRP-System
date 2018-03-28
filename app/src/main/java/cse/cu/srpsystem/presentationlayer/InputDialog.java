package cse.cu.srpsystem.presentationlayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

public class InputDialog extends DialogFragment {
    private String title;
    private int inputType;
    private Receiver receiver;
    private EditText inputField;

    public InputDialog setProperties(int inputType, String title, Receiver receiver) {
        this.inputType = inputType;
        this.title = title;
        this.receiver = receiver;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inputField = new EditText(getActivity());
        inputField.setInputType(inputType);
        return new AlertDialog.Builder(getActivity()).setView(inputField).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (receiver != null) receiver.receive(inputField.getText().toString());
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (receiver != null) receiver.onCancel();
            }
        }).setTitle(title).create();
    }

    public interface Receiver {
        void receive(String input);

        void onCancel();
    }
}