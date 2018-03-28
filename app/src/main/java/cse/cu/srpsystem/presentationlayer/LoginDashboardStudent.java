package cse.cu.srpsystem.presentationlayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import java.util.List;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.dataaccesslayer.LocalDAL;
import cse.cu.srpsystem.entities.Exam;
import cse.cu.srpsystem.applicationlayer.LoginBL;

public class LoginDashboardStudent extends LoginDashboardPL {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_student);
        findViewById(R.id.opt_view_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InputDialog().setProperties(InputType.TYPE_CLASS_NUMBER, "Enter student ID", new InputDialog.Receiver() {
                    @Override
                    public void receive(final String input) {
                        if (input.equalsIgnoreCase(LoginBL.getInstance().getUser().getUserId() + "")) {
                            List<Exam> exams = LocalDAL.getInstance(getApplicationContext()).getPublishedExamsByStudent(Integer.parseInt(input));
                            if (exams.size() == 0) {
                                UITools.showMessage(context, "It seems no exam you associate has published result.\n" +
                                        "Please refresh local data and try again.");
                            } else {
                                UITools.selectOne(context, exams, new UITools.SelectionListener() {
                                    @Override
                                    public void onSelect(List<?> selectedItems) {
                                        Intent intent = new Intent(context, ViewResultPL.class);
                                        intent.putExtra("student_id", Integer.parseInt(input));
                                        intent.putExtra("exam_id", ((Exam) selectedItems.get(0)).id);
                                        startActivity(intent);
                                    }
                                }, "Select Examination");
                            }
                        } else {
                            UITools.showMessage(context, "Sorry! You have to enter your own ID.\n" +
                                    "Because, students are restricted within his own results only.");
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show(getSupportFragmentManager(), "InputStudentID");
            }
        });
    }
}
