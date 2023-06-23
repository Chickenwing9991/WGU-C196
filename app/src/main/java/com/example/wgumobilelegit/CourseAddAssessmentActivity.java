package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.AssessmentAdapter;
import com.example.wgumobilelegit.Objects.Assessment;
import com.example.wgumobilelegit.Objects.AssessmentType;
import com.example.wgumobilelegit.Objects.CourseStatus;
import com.example.wgumobilelegit.dao.AssessmentDAO;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.TermDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.LocalDate;
import java.util.List;


public class CourseAddAssessmentActivity extends Activity implements AssessmentAdapter.OnAssessmentSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String TermTitle;
    public Assessment selectedAssessment;

    @Override
    public void onAssessmentSelected(Assessment selectedAssessment) {
        // This method will be called when an item is selected
        this.selectedAssessment = selectedAssessment;
        Log.d("Troubleshooting", "onAssessmentSelected: " + selectedAssessment.getAssessmentName() + ", Status: " + selectedAssessment.getAssessmenType());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_assessment_selection);

        Intent intent = getIntent();
        int CourseID = intent.getIntExtra("Course", 1);
        String Title = intent.getStringExtra("Title");
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));
        CourseStatus Status = CourseStatus.fromString(intent.getStringExtra("Status"));
        String Note = intent.getStringExtra("Note");

        StartDateValue = StartDateParam; // Initialize StartDateValue
        EndDateValue = EndDateParam;

        RecyclerView recyclerView = findViewById(R.id.CourseAssessmentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        AssessmentDAO assessmentDAO = db.AssessmentDAO();

        final List<Assessment> assessments = assessmentDAO.getAllAssessments();

        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, this); // pass 'this' as the listener
        recyclerView.setAdapter(assessmentAdapter);

        Button backButton = findViewById(R.id.AddCourseAssessmentBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseAddAssessmentActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });


        Button AddAssessment = findViewById(R.id.AddAssessmentToCourse);
        AddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Title = selectedAssessment.getAssessmentName();
                Integer AssessmentID = selectedAssessment.getAssessmentId();
                AssessmentType Type = selectedAssessment.getAssessmenType();
                Log.d("Troubleshooting", "Status "+String.valueOf(Type));
                EndDateValue = selectedAssessment.getDueDate();

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                AssessmentDAO assessmentDAO = db.AssessmentDAO();
                CourseDAO courseDAO = db.courseDAO();

                Assessment assessment = new Assessment(AssessmentID, CourseID, Title, Type, EndDateValue); // your assessment object
                assessmentDAO.update(assessment);

                Intent intent = new Intent(CourseAddAssessmentActivity.this, CourseViewActivity.class);

                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", Title);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);

                startActivity(intent);
            }
        });
    }
}
