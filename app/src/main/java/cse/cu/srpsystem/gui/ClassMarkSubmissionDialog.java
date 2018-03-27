package cse.cu.srpsystem.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.entities.Course;
import cse.cu.srpsystem.entities.Exam;
import cse.cu.srpsystem.logics.StatusListener;
import cse.cu.srpsystem.logics.SubmissionLogic;
import cse.cu.srpsystem.logics.ThreadLogics;

public class ClassMarkSubmissionDialog extends DialogFragment {
    private View layout;
    private Context context;
    private Button addButton, doneButton;
    private EditText marksField;
    private AutoCompleteTextView studentID;
    private ProgressBar progressBar;
    private SubmissionLogic logic;
    private ThreadLogics.CustomThread submissionThread;
    private Spinner courseList, examList;
    private ArrayAdapter<Course> courseAdapter;
    private ArrayAdapter<Exam> examAdapter;
    private ArrayAdapter<Integer> autoCompleteAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        logic = new SubmissionLogic(0);
        List<Exam> exams = logic.getExamsByCourseTeacher(context);
        if (exams.size() == 0)
            return new AlertDialog.Builder(context).setMessage("Sorry! No exam for courses you teach was found.\n" +
                    "Please refresh local data and try again.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            }).create();
        List<Course> courses = logic.getCoursesByTeacher(context);
        if (courses.size() == 0)
            return new AlertDialog.Builder(context).setMessage("Error! No course found!\n" +
                    "Please refresh local data and try again.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            }).create();
        autoCompleteAdapter = new ArrayAdapter<>(context, R.layout.sample_text, logic.getStudentIDsByCourseTeacher(context));
        if (autoCompleteAdapter.getCount() == 0)
            return new AlertDialog.Builder(context).setMessage("Error! No student found.\n" +
                    "Please refresh local data and try again.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            }).create();

        layout = LayoutInflater.from(context).inflate(R.layout.submit_class_marks, null);
        (studentID = layout.findViewById(R.id.student_id_field)).setAdapter(autoCompleteAdapter);
        (courseList = layout.findViewById(R.id.course_list)).setAdapter(courseAdapter = new ArrayAdapter<>(
                context, R.layout.sample_text, courses
        ));
        (examList = layout.findViewById(R.id.exam_list)).setAdapter(examAdapter = new ArrayAdapter<>(
                context, R.layout.sample_text, exams
        ));
        studentID = layout.findViewById(R.id.student_id_field);
        marksField = layout.findViewById(R.id.marks_field);
        (addButton = layout.findViewById(R.id.add_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                Course course = (Course) courseList.getSelectedItem();
                if (studentID.getText().length() == 0 || marksField.getText().length() == 0) {
                    UITools.showMessage(context, "You must provide student ID as well as marks.");
                    v.setEnabled(true);
                    return;
                }
                int student_id = Integer.parseInt(studentID.getText().toString());
                float marks = Float.parseFloat(marksField.getText().toString());
                if (course.type == 'T' && (marks < 0 || marks > course.credits * 25 * 0.3))
                    UITools.showMessage(context, "Sorry, marks for the selected course ranges [0, " + (course.credits * 25 * 0.3) + "]");
                else if (course.type == 'L' && (marks < 0 || marks > course.credits * 25))
                    UITools.showMessage(context, "Sorry, marks for the selected course ranges [0, " + (course.credits * 25) + "]");
                else if (logic.addClassMarks(context, student_id,
                        ((Exam) examList.getSelectedItem()).id, course.code, marks)) {
                    UITools.showToast(context, "Marks Added/Updated");
                    autoCompleteAdapter.remove(student_id);
                } else UITools.showMessage(context, "Error! Cannot add marks.\n" +
                        "Please make sure you provided valid input.");
                v.setEnabled(true);
            }
        });
        progressBar = layout.findViewById(R.id.progress);
        (doneButton = layout.findViewById(R.id.done_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITools.confirm(context, "Are you sure to submit ?", new UITools.ConfirmListener() {
                    @Override
                    public void ifYes() {
                        addButton.setEnabled(false);
                        doneButton.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        setCancelable(false);
                        (submissionThread = new ThreadLogics.CustomThread(new ThreadLogics.CustomThread.Runnable() {
                            @Override
                            public StatusListener.Status run(Object... parameters) {
                                return logic.submitAllClassMarks(context);
                            }
                        }, new StatusListener() {
                            @Override
                            public void listen(Status status) {
                                switch (status) {
                                    case SUCCESSFUL:
                                        UITools.showMessage(context, "Operation Successful.");
                                        dismiss();
                                        return;
                                    case ERR_CONNECTION_FAILED:
                                        UITools.showMessage(context, "Error! Cannot connect to server.");
                                        break;
                                    case ERR_PERMISSION_DENIED:
                                        UITools.showMessage(context, "Error! Server rejected your submission.");
                                        break;
                                    case UNKNOWN_ERROR:
                                    default:
                                        UITools.showMessage(context, "Oops! Unknown error.");

                                }
                                addButton.setEnabled(true);
                                doneButton.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                setCancelable(true);
                            }
                        })).execute();
                    }

                    @Override
                    public void ifNo() {

                    }
                });
            }
        });

        return new AlertDialog.Builder(context).setView(layout).setTitle("Submit Class/Lab Marks").create();
    }

    @Override
    public void onDestroy() {
        if (submissionThread != null) submissionThread.cancel(true);
        super.onDestroy();
    }
}
