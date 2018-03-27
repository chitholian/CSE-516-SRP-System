package cse.cu.srpsystem.gui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.data.LocalDataHandler;
import cse.cu.srpsystem.entities.Exam;
import cse.cu.srpsystem.logics.AuthLogic;
import cse.cu.srpsystem.logics.GenerationLogic;
import cse.cu.srpsystem.logics.StatusListener;
import cse.cu.srpsystem.logics.ThreadLogics;

public class DashboardExamController extends DashboardCommon {
    private ThreadLogics.CustomThread publicationThread, calculationThread;
    private List<Exam> exams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exams = LocalDataHandler.getInstance(this).getExamsByController(AuthLogic.getInstance().getUser().getUserId());
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

                            new DistributionDialog().setExam(((Exam) selectedItems.get(0)).id).show(getSupportFragmentManager(), "DistributionDialog");
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
                } else
                    UITools.selectOne(context, exams, new UITools.SelectionListener() {
                        @Override
                        public void onSelect(final List<?> selectedItems) {
                            final Dialog progress = UITools.createProgressWindow(context);
                            progress.setTitle("Calculating");
                            progress.show();
                            (calculationThread = new ThreadLogics.CustomThread(new ThreadLogics.CustomThread.Runnable() {
                                @Override
                                public StatusListener.Status run(Object... parameters) {
                                    return GenerationLogic.calculateGPA(getApplicationContext(), ((Exam) selectedItems.get(0)).id);
                                }
                            }, new StatusListener() {
                                @Override
                                public void listen(Status status) {
                                    progress.dismiss();
                                    if (status.equals(Status.SUCCESSFUL)) {
                                        UITools.showMessage(context, "Operation Successful.");
                                    } else if (status.equals(Status.ERR_CONNECTION_FAILED))
                                        UITools.showMessage(context, "Error! Connection failed.");
                                    else
                                        UITools.showMessage(context, "Oops! Unknown error.");
                                }
                            })).execute();
                        }
                    }, "Select Examination");
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
                List<Exam> unpublishedExams = new ArrayList<>();
                for (Exam e : exams)
                    if (e.published == 0) unpublishedExams.add(e);
                if (unpublishedExams.size() == 0) {
                    UITools.showMessage(context,
                            "No exam available to publish result.");
                } else
                    UITools.selectOne(context, unpublishedExams, new UITools.SelectionListener() {
                        @Override
                        public void onSelect(final List<?> selectedItems) {
                            UITools.confirm(context, "Are you sure to publish result ?",
                                    new UITools.ConfirmListener() {
                                        @Override
                                        public void ifYes() {
                                            final Dialog progressDialog = UITools.createProgressWindow(context);
                                            progressDialog.setCancelable(false);
                                            progressDialog.setTitle("Please wait");
                                            progressDialog.show();
                                            (publicationThread = new ThreadLogics.CustomThread(new ThreadLogics.CustomThread.Runnable() {
                                                @Override
                                                public StatusListener.Status run(Object... parameters) {
                                                    return GenerationLogic.publishResult(context, ((Exam) selectedItems.get(0)).id);
                                                }
                                            }, new StatusListener() {
                                                @Override
                                                public void listen(Status status) {
                                                    progressDialog.dismiss();
                                                    if (status.equals(Status.SUCCESSFUL)) {
                                                        ((Exam) selectedItems.get(0)).published = 1;
                                                        UITools.showMessage(context, "Operation Successful.");
                                                    } else if (status.equals(Status.ERR_CONNECTION_FAILED))
                                                        UITools.showMessage(context, "Error! Connection failed.");
                                                    else
                                                        UITools.showMessage(context, "Oops! Unknown error.");
                                                }
                                            })).execute();
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
                        List<Exam> exams = LocalDataHandler.getInstance(getApplicationContext()).getPublishedExamsByStudent(Integer.parseInt(input));
                        if (exams.size() == 0) {
                            UITools.showMessage(context, "It seems no exam the Student ID associates has published result.\n" +
                                    "Please refresh local data and try again.");
                        } else {
                            UITools.selectOne(context, exams, new UITools.SelectionListener() {
                                @Override
                                public void onSelect(List<?> selectedItems) {
                                    Intent intent = new Intent(context, ResultViewActivity.class);
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
        if (publicationThread != null)
            publicationThread.cancel(true);
        if (calculationThread != null)
            calculationThread.cancel(true);

        super.onDestroy();
    }

    @Override
    protected void refreshData() {
        super.refreshData();
        exams = LocalDataHandler.getInstance(this).getExamsByController(AuthLogic.getInstance().getUser().getUserId());
    }
}
