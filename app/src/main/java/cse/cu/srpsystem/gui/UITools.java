package cse.cu.srpsystem.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.logics.AuthLogic;

public class UITools {

    public static void showMessage(Context context, String msg) {
        showMessage(context, msg, false);
    }

    public static void showMessage(final Context context, String msg, final boolean finishActivity) {
        (new AlertDialog.Builder(context)).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage(msg).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (finishActivity) ((Activity) context).finish();
            }
        }).create().show();
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void confirm(Context context, String msg, final ConfirmListener listener) {
        (new AlertDialog.Builder(context)).setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.ifNo();
                dialog.dismiss();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null)
                    listener.ifYes();
                dialog.dismiss();
            }
        }).setMessage(msg).setCancelable(false).create().show();
    }

    public static void selectOne(Context context, final List<?> objects, final SelectionListener listener, String title) {
        final ArrayList<Object> selected = new ArrayList<>();
        selected.add(null);
        new AlertDialog.Builder(context).setSingleChoiceItems(new ArrayAdapter<>(context, R.layout.sample_text, objects),
                0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selected.set(0, objects.get(which));
                        dialog.dismiss();
                        if (listener != null)
                            listener.onSelect(selected);
                    }
                }).setTitle(title).create().show();
    }

    public static void openDashboard(Context context, boolean refreshData) {
        Intent intent = null;
        switch (AuthLogic.getInstance().getUser().getRole()) {
            case "Exam Controller":
                intent = new Intent(context, DashboardExamController.class);
                break;
            case "Teacher":
                intent = new Intent(context, DashboardTeacher.class);
                break;
            case "Student":
                intent = new Intent(context, DashboardStudent.class);
                break;
            default:
                UITools.showMessage(context, "Oops! cannot identify your role. It seems you are not logged in.");
                return;
        }
        intent.putExtra("refresh_data", refreshData);
        context.startActivity(intent);
    }

    public static Dialog createProgressWindow(Context context) {
        ProgressBar bar = new ProgressBar(context);
        bar.setIndeterminate(true);
        return new AlertDialog.Builder(context).setView(bar).setCancelable(false).create();
    }


    public interface SelectionListener {
        void onSelect(List<?> selectedItems);
    }

    public interface ConfirmListener {
        void ifYes();

        void ifNo();
    }
}

