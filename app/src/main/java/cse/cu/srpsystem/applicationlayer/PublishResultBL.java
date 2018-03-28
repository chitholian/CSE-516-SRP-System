package cse.cu.srpsystem.applicationlayer;


import android.content.Context;


import cse.cu.srpsystem.dataaccesslayer.LocalDAL;
import cse.cu.srpsystem.dataaccesslayer.RemoteDAL;

public class PublishResultBL {
    public static StatusListener.Status publishResult(Context context, int exam_id) {
        StatusListener.Status status = RemoteDAL.getInstance().publishResult(exam_id);
        if (status.equals(StatusListener.Status.SUCCESSFUL))
            LocalDAL.getInstance(context).publishResult(exam_id);
        return status;
    }
}
