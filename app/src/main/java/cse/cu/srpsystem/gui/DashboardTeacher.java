package cse.cu.srpsystem.gui;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.data.LocalDataHandler;
import cse.cu.srpsystem.entities.Exam;
import cse.cu.srpsystem.logics.AuthLogic;

public class DashboardTeacher extends DashboardCommon {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_teacher);
        findViewById(R.id.btn_submit_script_marks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Exam> exams = LocalDataHandler.getInstance(context)
                        .getExamsByExaminer(AuthLogic.getInstance().getUser().getUserId());
                if (exams.size() == 0) {
                    UITools.showMessage(context,
                            "Error! No exam found.\n" +
                                    "Please refresh local data and try again.\n" +
                                    "Also make sure you are assigned with any answer-script.");
                } else
                    UITools.selectOne(context, exams, new UITools.SelectionListener() {
                        @Override
                        public void onSelect(List<?> selectedItems) {
                            new ScriptMarkSubmissionDialog().setExam(((Exam) selectedItems.get(0)).id).show(getSupportFragmentManager(), "DistributionDialog");
                        }
                    }, "Select Examination");
            }
        });
        findViewById(R.id.btn_submit_class_marks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClassMarkSubmissionDialog().show(getSupportFragmentManager(), "Class-Marks Submission");
            }
        });
    }
}
