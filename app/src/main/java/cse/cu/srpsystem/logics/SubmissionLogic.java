package cse.cu.srpsystem.logics;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cse.cu.srpsystem.data.LocalDataHandler;
import cse.cu.srpsystem.data.RemoteDataHandler;
import cse.cu.srpsystem.entities.AnswerScript;
import cse.cu.srpsystem.entities.ClassMarks;
import cse.cu.srpsystem.entities.Course;
import cse.cu.srpsystem.entities.Exam;
import cse.cu.srpsystem.entities.Teacher;

public class SubmissionLogic {
    private int exam_id;

    public SubmissionLogic(int exam_id) {
        this.exam_id = exam_id;
    }

    public StatusListener.Status submitAll(Context context) {
        List<AnswerScript> scripts = LocalDataHandler.getInstance(context).getAnswerScriptsByExaminer(AuthLogic.getInstance().getUser().getUserId());
        return RemoteDataHandler.getInstance().sendScriptsAsExaminer(scripts);
    }

    public List<Integer> getScriptCodes(Context context) {
        return LocalDataHandler.getInstance(context).getScriptCodesByExaminer(AuthLogic.getInstance().getUser().getUserId(), exam_id);
    }

    public boolean addMarks(Context context, int code, float marks) {
        return LocalDataHandler.getInstance(context).updateScriptMarks(exam_id, code, marks, AuthLogic.getInstance().getUser().getUserId());
    }

    public List<Course> getCoursesByTeacher(Context context) {
        return LocalDataHandler.getInstance(context).getCoursesByTeacher(AuthLogic.getInstance().getUser().getUserId());
    }

    public boolean addClassMarks(Context context, int student_id, int exam_id, String course_code, float marks) {
        return LocalDataHandler.getInstance(context).addClassMarks(student_id, exam_id, course_code + "", marks);
    }

    public StatusListener.Status submitAllClassMarks(Context context) {
        List<ClassMarks> marks = LocalDataHandler.getInstance(context).getClassMarksByTeacher(
                AuthLogic.getInstance().getUser().getUserId()
        );
        return RemoteDataHandler.getInstance().sendClassMarks(marks);
    }

    public List<Exam> getExamsByCourseTeacher(Context context) {
        return LocalDataHandler.getInstance(context).getExamsOfCourseTeacher(AuthLogic.getInstance().getUser().getUserId());
    }

    public List<Integer> getStudentIDsByCourseTeacher(Context context) {
        return LocalDataHandler.getInstance(context).getStudentIDsByCourseTeacher(AuthLogic.getInstance().getUser().getUserId());
    }
}
