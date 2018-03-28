package cse.cu.srpsystem.presentationlayer;

import android.app.Dialog;
import android.content.Context;

import cse.cu.srpsystem.applicationlayer.PublishResultBL;
import cse.cu.srpsystem.applicationlayer.StatusListener;
import cse.cu.srpsystem.applicationlayer.ThreadRunnerBL;
import cse.cu.srpsystem.entities.Exam;

public class PublishResultPL implements Observable, Observer {
    private ThreadRunnerBL.CustomThread publicationThread;

    public void publish(final Context context, final Exam exam) {
        final Dialog progressDialog = UITools.createProgressWindow(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");
        progressDialog.show();
        (publicationThread = new ThreadRunnerBL.CustomThread(new ThreadRunnerBL.CustomThread.Runnable() {
            @Override
            public StatusListener.Status run(Object... parameters) {
                return PublishResultBL.publishResult(context, exam.id);
            }
        }, new StatusListener() {
            @Override
            public void listen(Status status) {
                progressDialog.dismiss();
                if (status.equals(Status.SUCCESSFUL)) {
                    exam.published = 1;
                    UITools.showMessage(context, "Operation Successful.");
                    onUpdate();
                } else if (status.equals(Status.ERR_CONNECTION_FAILED))
                    UITools.showMessage(context, "Error! Connection failed.");
                else
                    UITools.showMessage(context, "Oops! Unknown error.");
            }
        })).execute();
    }

    @Override
    public void onUpdate() {
        for (Observer o : observers)
            if (o != null) o.observe(this);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void observe(Observable subject) {
        if (publicationThread != null) publicationThread.cancel(true);
    }
}
