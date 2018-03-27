package cse.cu.srpsystem.entities;

public class ClassMarks {
    public int exam_id, student_id;
    public float marks;
    public String course_code;

    @Override
    public String toString() {
        return course_code + " : " + marks;
    }
}
