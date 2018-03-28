package cse.cu.srpsystem.presentationlayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import java.util.List;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.applicationlayer.PublishResultBL;
import cse.cu.srpsystem.dataaccesslayer.LocalDAL;
import cse.cu.srpsystem.entities.Exam;
import cse.cu.srpsystem.applicationlayer.LoginBL;
import cse.cu.srpsystem.applicationlayer.ThreadRunnerBL;

public class LoginDashboardExamController extends LoginDashboardPL implements Observable {
    private ThreadRunnerBL.CustomThread publicationThread, calculationThread;
    private List<Exam> exams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveExams();
        setContentView(R.layout.dashboard_exam_controller);
        findViewById(R.id.opt_distribute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exams.size() == 0) {
                    UITools.showMessage(context,
                            "Error! No exam found.\n" +
                                    "Please refresh local data and try again.\n" +
                                    "Also make sure if you are assigned as controller of the exam(s).");
                } else
                    UITools.selectOne(context, exams, new UITools.SelectionListener() {
                        @Override
                        public void onSelect(List<?> selectedItems) {
                            new DistributeAnswerScriptsPL().setExam(((Exam) selectedItems.get(0)).id).show(getSupportFragmentManager(), "DistributeAnswerScriptsPL");
                        }
                    }, "Select Examination");
            }
        });
        findViewById(R.id.opt_calc_gpa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exams.size() == 0) {
                    UITools.showMessage(context,
                            "Error! No exam found.\n" +
                                    "Please refresh local data and try again.\n" +
                                    "Also make sure if you are assigned as controller of the exam(s).");
                } else {
                    CalculateGPAPL.calculate(context, exams);
                }
            }
        });

        findViewById(R.id.opt_gen_transcript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITools.showToast(context, "Not implemented yet.");

                /*if (exams.size() == 0) {
                    UITools.showMessage(context,
                            "Error! No exam found.\n" +
                                    "Please refresh local data and try again.\n" +
                                    "Also make sure if you are assigned as controller of the exam(s).");
                } else
                    UITools.selectOne(context, exams, new UITools.SelectionListener() {
                        @Override
                        public void onSelect(List<?> selectedItems) {

                        }
                    }, "Select Examination");*/
            }
        });

        findViewById(R.id.opt_publish_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exams.size() == 0) {
                    UITools.showMessage(context,
                            "No exam available to publish result.");
                } else
                    UITools.selectOne(context, exams, new UITools.SelectionListener() {
                        @Override
                        public void onSelect(final List<?> selectedItems) {
                            UITools.confirm(context, "Are you sure to publish result ?",
                                    new UITools.ConfirmListener() {
                                        @Override
                                        public void ifYes() {
                                            new PublishResultPL().publish(context, ((Exam) selectedItems.get(0)));
                                        }

                                        @Override
                                        public void ifNo() {

                                        }
                                    });
                        }
                    }, "Select Examination");
            }
        });

        findViewById(R.id.opt_view_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InputDialog().setProperties(InputType.TYPE_CLASS_NUMBER, "Enter Student ID", new InputDialog.Receiver() {
                    @Override
                    public void receive(final String input) {
                        List<Exam> exams = LocalDAL.getInstance(getApplicationContext()).getPublishedExamsByStudent(Integer.parseInt(input));
                        if (exams.size() == 0) {
                            UITools.showMessage(context, "It seems no exam the Student ID associates has published result.\n" +
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
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show(getSupportFragmentManager(), "InputStudentID");
            }
        });
    }

    @Override
    protected void onDestroy() {
        onUpdate();
        super.onDestroy();
    }

    @Override
    protected void refreshData() {
        super.refreshData();
        retrieveExams();
    }

    private void retrieveExams() {
        exams = LocalDAL.getInstance(this).getExamsByController(LoginBL.getInstance().getUser().getUserId());
    }

    @Override
    public void onUpdate() {
        for (Observer observer : observers)
            if (observer != null)
                observer.observe(this);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}
