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

// Class for adding assessments to a course
public class CourseAddAssessmentActivity extends Activity implements AssessmentAdapter.OnAssessmentSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String TermTitle;
    public Assessment selectedAssessment;

    // Method to handle the selection of an assessment
    @Override
    public void onAssessmentSelected(Assessment selectedAssessment) {
        this.selectedAssessment = selectedAssessment;
        Log.d("Troubleshooting", "onAssessmentSelected: " + selectedAssessment.getAssessmentName() + ", Status: " + selectedAssessment.getAssessmenType());
    }

    // Method to handle the creation of the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_assessment_selection);

        // Get the intent and extract the data
        Intent intent = getIntent();
        int CourseID = intent.getIntExtra("CourseID", 1);
        String CourseTitle = intent.getStringExtra("Title");
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));
        CourseStatus Status = CourseStatus.fromString(intent.getStringExtra("Status"));
        String Note = intent.getStringExtra("Note");

        // Initialize StartDateValue and EndDateValue
        StartDateValue = StartDateParam;
        EndDateValue = EndDateParam;

        // Set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.CourseAssessmentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get the database instance and the DAO
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        AssessmentDAO assessmentDAO = db.AssessmentDAO();

        // Get all assessments
        final List<Assessment> assessments = assessmentDAO.getAllAssessments();

        // Set up the adapter and set it to the RecyclerView
        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, this);
        recyclerView.setAdapter(assessmentAdapter);

        // Set up the back button
        Button backButton = findViewById(R.id.AddCourseAssessmentBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create a new intent and put the data
                Intent intent = new Intent(CourseAddAssessmentActivity.this, CourseViewActivity.class);

                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", CourseTitle);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);

                // Start the new activity
                startActivity(intent);
            }
        });

        // Set up the AddAssessment button
        Button AddAssessment = findViewById(R.id.AddAssessmentToCourse);
        AddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the data from the selected assessment
                String Title = selectedAssessment.getAssessmentName();
                Integer AssessmentID = selectedAssessment.getAssessmentId();
                AssessmentType Type = selectedAssessment.getAssessmenType();
                EndDateValue = selectedAssessment.getDueDate();

                // Get the database instance and the DAO
                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                AssessmentDAO assessmentDAO = db.AssessmentDAO();
                CourseDAO courseDAO = db.courseDAO();

                // Create a new assessment and insert it into the database
                Assessment assessment = new Assessment(CourseID, Title, Type, EndDateValue);
                assessmentDAO.insert(assessment);

                // Create a new intent and put the data
                Intent intent = new Intent(CourseAddAssessmentActivity.this, CourseViewActivity.class);

                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", CourseTitle);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);

                // Start the new activity
                startActivity(intent);
            }
        });
    }
}
