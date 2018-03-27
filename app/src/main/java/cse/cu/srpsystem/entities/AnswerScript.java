package cse.cu.srpsystem.entities;

public class AnswerScript {
    public String course_code;
    public int exam_id, examiner_id, student_id, code;
    public float marks;
    public char part;

    @Override
    public String toString() {
        return code + "";
    }
}
