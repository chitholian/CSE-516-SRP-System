package cse.cu.srpsystem.applicationlayer;

import android.content.Context;

import java.util.List;

import cse.cu.srpsystem.dataaccesslayer.LocalDAL;
import cse.cu.srpsystem.entities.Result;

public class ViewResultBL {
    public static List<Result> getResults(Context context, int student_id, int exam_id) {
        return LocalDAL.getInstance(context).getResult(student_id, exam_id);
    }
}
