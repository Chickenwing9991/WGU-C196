package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.AssessmentAdapter;
import com.example.wgumobilelegit.Adapters.CourseAdapter;
import com.example.wgumobilelegit.Objects.Assessment;
import com.example.wgumobilelegit.Objects.Course;
import com.example.wgumobilelegit.Objects.CourseStatus;
import com.example.wgumobilelegit.dao.AssessmentDAO;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.LocalDate;
import java.util.List;


public class CourseViewActivity extends Activity implements AssessmentAdapter.OnAssessmentSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String CourseTitle;
    public Assessment selectedAssessment;

    @Override
    public void onAssessmentSelected(Assessment selectedAssessment) {
        // This method will be called when an item is selected
        this.selectedAssessment = selectedAssessment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        TextView Notes = findViewById(R.id.courseDetailsNotesValue);

        //Get DB Access
        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getDbInstance(context);

        CourseDAO courseDAO = db.courseDAO();
        AssessmentDAO assessmentDAO = db.AssessmentDAO();
        /////

        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        int CourseID = intent.getIntExtra("CourseID", 1);
        String Title = intent.getStringExtra("Title");
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));
        CourseStatus Status = CourseStatus.fromString(intent.getStringExtra("Status"));
        String Note = intent.getStringExtra("Note");

        Notes.setText(Note);

        StartDateValue = StartDateParam; // Initialize StartDateValue
        EndDateValue = EndDateParam;

        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.detailCourseName);
        title.setText(Title);

        TextView startDate = findViewById(R.id.courseDetailsStartValue);
        startDate.setText(String.valueOf(StartDateParam));

        TextView endDate = findViewById(R.id.courseDetailsEndValue);
        endDate.setText(String.valueOf(EndDateParam));

        TextView StatusVal = findViewById(R.id.courseDetailsStatusValue);
        StatusVal.setText(Status.toString());

        RecyclerView recyclerView = findViewById(R.id.courseDetailsAssessments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<Assessment> assessments = assessmentDAO.getAssociatedAssessments(CourseID);
        Log.d("TroubleShoot", "Ass List"+assessments);

        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, this); // pass 'this' as the listener
        recyclerView.setAdapter(assessmentAdapter);


        Button backButton = findViewById(R.id.detailBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseViewActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });

        Button AddAssessment = findViewById(R.id.courseDetailsAddAssessment);
        AddAssessment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseViewActivity.this, CourseAddAssessmentActivity.class);

                Log.d("Troubleshoot", "Start Date"+StartDateValue);


                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", Title);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);

                startActivity(intent);
            }
        });

        Button DeleteCourse = findViewById(R.id.courseDetailsDeleteAssessment);
        DeleteCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                selectedAssessment.setClassID(null);

                assessmentDAO.update(selectedAssessment);

                final List<Assessment> assessments = assessmentDAO.getAssociatedAssessments(CourseID);

                AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, CourseViewActivity.this); // pass 'this' as the listener
                recyclerView.setAdapter(assessmentAdapter);
            }
        });
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String IsDate = data.getStringExtra("Date");
        Log.d("Troubleshooting", String.valueOf(IsDate));

        if (resultCode == RESULT_OK && Objects.equals(IsDate, "Start")) {
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            StartDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView StartText = findViewById(R.id.editStartDate);
            StartText.setText(String.valueOf(StartDateValue));

            Log.d("Troubleshooting", String.valueOf(StartDateValue));
            Log.d("Troubleshooting", String.valueOf("Start"));
        }
        else{
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            EndDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView EndText = findViewById(R.id.editEndDate);
            EndText.setText(String.valueOf(EndDateValue));

            Log.d("Troubleshooting", String.valueOf(EndDateValue));
            Log.d("Troubleshooting", String.valueOf("End"));
        }
    }
    */
}
