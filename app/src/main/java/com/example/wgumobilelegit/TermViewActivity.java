package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.CourseAdapter;
import com.example.wgumobilelegit.Adapters.TermAdapter;
import com.example.wgumobilelegit.Objects.Course;
import com.example.wgumobilelegit.Objects.Term;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.TermDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;


public class TermViewActivity extends Activity implements CourseAdapter.OnCourseSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String TermTitle;
    public Course selectedCourse;

    // Method to handle course selection
    @Override
    public void onCourseSelected(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    // Method to handle activity creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details);

        // Get database instance
        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getDbInstance(context);

        CourseDAO courseDAO = db.courseDAO();

        // Extract data from intent
        Intent intent = getIntent();
        int TermID = intent.getIntExtra("TermID", 1);
        String Title = intent.getStringExtra("Title");
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));

        StartDateValue = StartDateParam; // Initialize StartDateValue
        EndDateValue = EndDateParam;

        // Set text views with extracted data
        TextView title = findViewById(R.id.detailTermName);
        title.setText(Title);

        TextView startDate = findViewById(R.id.termDetailsStartValue);
        startDate.setText(String.valueOf(StartDateParam));

        TextView endDate = findViewById(R.id.termDetailsEndValue);
        endDate.setText(String.valueOf(EndDateParam));

        // Set up recycler view
        RecyclerView recyclerView = findViewById(R.id.termDetailsCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<Course> courses = courseDAO.getAssociatedCourses(TermID);

        CourseAdapter courseAdapter = new CourseAdapter(courses, this); // pass 'this' as the listener
        recyclerView.setAdapter(courseAdapter);

        // Set up back button
        Button backButton = findViewById(R.id.detailBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TermViewActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });

        // Set up add course button
        Button AddCourse = findViewById(R.id.termDetailsAddCourse);
        AddCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TermViewActivity.this, TermAddCourseActivity.class);
                intent.putExtra("TermID", TermID);
                startActivity(intent);
            }
        });

        // Set up delete course button
        Button DeleteCourse = findViewById(R.id.termDetailsDeleteCourse);
        DeleteCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedCourse.setTermID(null);
                courseDAO.delete(selectedCourse);
                final List<Course> courses = courseDAO.getAssociatedCourses(TermID);
                CourseAdapter courseAdapter = new CourseAdapter(courses, TermViewActivity.this); // pass 'this' as the listener
                recyclerView.setAdapter(courseAdapter);
            }
        });

    }

    // Method to handle result from another activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String IsDate = data.getStringExtra("Date");

        if (resultCode == RESULT_OK && Objects.equals(IsDate, "Start")) {
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            StartDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView StartText = findViewById(R.id.editStartDate);
            StartText.setText(String.valueOf(StartDateValue));
        }
        else{
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            EndDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView EndText = findViewById(R.id.editEndDate);
            EndText.setText(String.valueOf(EndDateValue));
        }
    }
}
