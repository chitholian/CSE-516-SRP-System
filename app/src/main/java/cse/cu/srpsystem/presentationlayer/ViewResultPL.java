package cse.cu.srpsystem.presentationlayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cse.cu.srpsystem.R;
import cse.cu.srpsystem.applicationlayer.CalculateGPABL;
import cse.cu.srpsystem.applicationlayer.ViewResultBL;
import cse.cu.srpsystem.entities.Result;
import cse.cu.srpsystem.applicationlayer.PublishResultBL;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ViewResultPL extends AppCompatActivity {
    private int exam_id, student_id;
    private List<Result> results;
    private TableView<String[]> tableView;
    private float cgpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        exam_id = getIntent().getIntExtra("exam_id", 0);
        student_id = getIntent().getIntExtra("student_id", 0);
        results = ViewResultBL.getResults(this, student_id, exam_id);
        if (results.size() == 0) {
            UITools.showMessage(ViewResultPL.this, "Oops! No result for the given ID found.", true);
            return;
        }
        ArrayList<String[]> gpaArray = new ArrayList<>();
        for (Result r : results)
            gpaArray.add(new String[]{r.course_code, String.format(Locale.ENGLISH, "%.2f", r.gpa)});
        tableView = findViewById(R.id.result_table);
        TableColumnModel model = new TableColumnWeightModel(2);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, "Course Code", "GPA"));
        model.getColumnWidth(0, 1);
        model.getColumnWidth(1, 1);
        tableView.setColumnModel(model);
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, gpaArray));

        cgpa = CalculateGPABL.calculateCGPA(this, results);
        ((TextView) findViewById(R.id.cgpa)).setText(String.format(Locale.ENGLISH, "ID: %d    CGPA: %.2f", student_id, cgpa));
    }
}
