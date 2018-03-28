package cse.cu.srpsystem.presentationlayer;

import android.app.Dialog;
import android.content.Context;

import java.util.List;

import cse.cu.srpsystem.applicationlayer.CalculateGPABL;
import cse.cu.srpsystem.applicationlayer.StatusListener;
import cse.cu.srpsystem.applicationlayer.ThreadRunnerBL;
import cse.cu.srpsystem.entities.Exam;

public class CalculateGPAPL implements Observer {
    private static ThreadRunnerBL.CustomThread calculationThread;

    public static void calculate(final Context context, List<Exam> exams) {
        UITools.selectOne(context, exams, new UITools.SelectionListener() {
            @Override
            public void onSelect(final List<?> selectedItems) {
                final Dialog progress = UITools.createProgressWindow(context);
                progress.setTitle("Calculating");
                progress.show();
                (calculationThread = new ThreadRunnerBL.CustomThread(new ThreadRunnerBL.CustomThread.Runnable() {
                    @Override
                    public StatusListener.Status run(Object... parameters) {
                        return CalculateGPABL.calculateGPA(context, ((Exam) selectedItems.get(0)).id);
                    }
                }, new StatusListener() {
                    @Override
                    public void listen(Status status) {
                        progress.dismiss();
                        if (status.equals(Status.SUCCESSFUL)) {
                            UITools.showMessage(context, "Operation Successful.");
                        } else if (status.equals(Status.ERR_CONNECTION_FAILED))
                            UITools.showMessage(context, "Error! Connection failed.");
                        else
                            UITools.showMessage(context, "Oops! Unknown error.");
                    }
                })).execute();
            }
        }, "Select Examination");
    }

    @Override
    public void observe(Observable subject) {
        if (calculationThread != null)
            calculationThread.cancel(true);
    }
}
