package cse.cu.srpsystem.entities;

public class Exam {
    public int id, controller_id;
    public String title, description;
    public short published;

    @Override
    public String toString() {
        return title;
    }
}
