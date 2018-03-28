package cse.cu.srpsystem.presentationlayer;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.dataaccesslayer.LocalDAL;
import cse.cu.srpsystem.entities.Exam;
import cse.cu.srpsystem.applicationlayer.LoginBL;

public class LoginDashboardTeacher extends LoginDashboardPL {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_teacher);
        findViewById(R.id.btn_submit_script_marks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Exam> exams = LocalDAL.getInstance(context)
                        .getExamsByExaminer(LoginBL.getInstance().getUser().getUserId());
                if (exams.size() == 0) {
                    UITools.showMessage(context,
                            "Error! No exam found.\n" +
                                    "Please refresh local data and try again.\n" +
                                    "Also make sure you are assigned with any answer-script.");
                } else
                    UITools.selectOne(context, exams, new UITools.SelectionListener() {
                        @Override
                        public void onSelect(List<?> selectedItems) {
                            new SubmitAnswerScriptMarksPL().setExam(((Exam) selectedItems.get(0)).id).show(getSupportFragmentManager(), "DistributeAnswerScriptsPL");
                        }
                    }, "Select Examination");
            }
        });
        findViewById(R.id.btn_submit_class_marks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SubmitLabMarksPL().show(getSupportFragmentManager(), "Class-Marks Submission");
            }
        });
    }
}
