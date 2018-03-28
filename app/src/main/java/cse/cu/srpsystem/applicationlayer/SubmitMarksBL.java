package cse.cu.srpsystem.applicationlayer;

import android.content.Context;

import java.util.List;

import cse.cu.srpsystem.dataaccesslayer.LocalDAL;
import cse.cu.srpsystem.dataaccesslayer.RemoteDAL;
import cse.cu.srpsystem.entities.AnswerScript;
import cse.cu.srpsystem.entities.ClassMarks;
import cse.cu.srpsystem.entities.Course;
import cse.cu.srpsystem.entities.Exam;

public class SubmitMarksBL {
    private int exam_id;

    public SubmitMarksBL(int exam_id) {
        this.exam_id = exam_id;
    }

    public StatusListener.Status submitAll(Context context) {
        List<AnswerScript> scripts = LocalDAL.getInstance(context).getAnswerScriptsByExaminer(LoginBL.getInstance().getUser().getUserId());
        return RemoteDAL.getInstance().sendScriptsAsExaminer(scripts);
    }

    public List<Integer> getScriptCodes(Context context) {
        return LocalDAL.getInstance(context).getScriptCodesByExaminer(LoginBL.getInstance().getUser().getUserId(), exam_id);
    }

    public boolean addMarks(Context context, int code, float marks) {
        return LocalDAL.getInstance(context).updateScriptMarks(exam_id, code, marks, LoginBL.getInstance().getUser().getUserId());
    }

    public List<Course> getCoursesByTeacher(Context context) {
        return LocalDAL.getInstance(context).getCoursesByTeacher(LoginBL.getInstance().getUser().getUserId());
    }

    public boolean addClassMarks(Context context, int student_id, int exam_id, String course_code, float marks) {
        return LocalDAL.getInstance(context).addClassMarks(student_id, exam_id, course_code + "", marks);
    }

    public StatusListener.Status submitAllClassMarks(Context context) {
        List<ClassMarks> marks = LocalDAL.getInstance(context).getClassMarksByTeacher(
                LoginBL.getInstance().getUser().getUserId()
        );
        return RemoteDAL.getInstance().sendClassMarks(marks);
    }

    public List<Exam> getExamsByCourseTeacher(Context context) {
        return LocalDAL.getInstance(context).getExamsOfCourseTeacher(LoginBL.getInstance().getUser().getUserId());
    }

    public List<Integer> getStudentIDsByCourseTeacher(Context context) {
        return LocalDAL.getInstance(context).getStudentIDsByCourseTeacher(LoginBL.getInstance().getUser().getUserId());
    }
}
