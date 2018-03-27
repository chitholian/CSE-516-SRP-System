package cse.cu.srpsystem.entities;

public class Course {
    public String code;
    public String title;
    public char type;
    public int credits;

    @Override
    public String toString() {
        return code;
    }
}
