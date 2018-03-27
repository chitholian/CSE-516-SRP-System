package cse.cu.srpsystem.logics;

import android.content.Context;

import java.util.List;
import java.util.Random;

import cse.cu.srpsystem.data.LocalDataHandler;
import cse.cu.srpsystem.data.RemoteDataHandler;
import cse.cu.srpsystem.entities.AnswerScript;

public class DistributionLogic {
    private int exam_id;
    private int code = 1000;
    private Random random = new Random(System.currentTimeMillis());

    public int getExamID() {
        return exam_id;
    }

    public DistributionLogic(int exam_id) {
        this.exam_id = exam_id;
    }

    public int generateCode(Context context) {
        if (code == 1000)
            code = Math.max(1000, LocalDataHandler.getInstance(context).getMaximumCode(exam_id));
        return code += 1 + random.nextInt(5);
    }

    public boolean assignCode(Context context, int teacher_id, int student_id, String course_code, char part) {
        AnswerScript script = new AnswerScript();
        script.code = code;
        script.examiner_id = teacher_id;
        script.exam_id = exam_id;
        script.course_code = course_code;
        script.student_id = student_id;
        script.part = part;
        return LocalDataHandler.getInstance(context).addScript(script);
    }

    public StatusListener.Status submitAll(Context context) {
        try {
            List<AnswerScript> scripts = LocalDataHandler.getInstance(context).getAnswerScriptsByController(
                    AuthLogic.getInstance().getUser().getUserId()
            );
            return RemoteDataHandler.getInstance().sendScripts(scripts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return StatusListener.Status.ERR_CONNECTION_FAILED;
    }
}
