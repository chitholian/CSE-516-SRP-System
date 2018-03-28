package cse.cu.srpsystem.applicationlayer;

import android.content.Context;

import java.util.List;

import cse.cu.srpsystem.dataaccesslayer.LocalDAL;
import cse.cu.srpsystem.dataaccesslayer.RemoteDAL;
import cse.cu.srpsystem.entities.Result;

public class CalculateGPABL {

    public static float calculateCGPA(Context context, List<Result> results) {
        float p = 0, c = 0;
        for (Result r : results) {
            float cr = LocalDAL.getInstance(context).getCredits(r.course_code);
            p += cr * r.gpa;
            c += cr;
        }
        return p / c;
    }

    public static float getGPA(float marks, float credits) {
        float percentage = 100 * marks / (credits * 25);
        if (percentage >= 80) return 4.00f;
        if (percentage >= 75) return 3.75f;
        if (percentage >= 70) return 3.50f;
        if (percentage >= 65) return 3.25f;
        if (percentage >= 60) return 3.00f;
        if (percentage >= 55) return 2.75f;
        if (percentage >= 50) return 2.50f;
        if (percentage >= 45) return 2.25f;
        if (percentage >= 40) return 2.00f;
        return 0.00f;
    }

    public static StatusListener.Status calculateGPA(Context context, int exam_id) {
        List<Result> allResults = LocalDAL.getInstance(context).getMarksAsResult(exam_id);
        StatusListener.Status status = RemoteDAL.getInstance().sendResults(allResults);
        if (status.equals(StatusListener.Status.SUCCESSFUL))
            LocalDAL.getInstance(context).storeResults(allResults);
        return status;
    }
}
