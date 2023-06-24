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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.CourseAdapter;
import com.example.wgumobilelegit.Objects.Course;
import com.example.wgumobilelegit.Objects.CourseStatus;
import com.example.wgumobilelegit.Objects.Term;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.TermDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

// Activity to add a course to a term
public class TermAddCourseActivity extends Activity implements CourseAdapter.OnCourseSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String TermTitle;
    public Course selectedCourse;

    // Method to handle course selection
    @Override
    public void onCourseSelected(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
        Log.d("Course Selection", "Selected Course: " + selectedCourse.getCourseName() + ", Status: " + selectedCourse.getCourseStatus());
    }

    // Method to handle activity creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_course_selection);

        // Get term ID from intent
        Intent intent = getIntent();
        int TermID = intent.getIntExtra("TermID", 1);

        // Set up RecyclerView for course list
        RecyclerView recyclerView = findViewById(R.id.TermCourseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get database instance and course DAO
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        CourseDAO courseDAO = db.courseDAO();

        // Get all courses
        final List<Course> courses = courseDAO.getAllCourses();

        // Set up course adapter and attach to RecyclerView
        CourseAdapter courseAdapter = new CourseAdapter(courses, this);
        recyclerView.setAdapter(courseAdapter);

        // Set up back button and its click listener
        Button backButton = findViewById(R.id.AddTermCourseBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TermAddCourseActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });

        // Set up AddCourse button and its click listener
        Button AddCourse = findViewById(R.id.AddCourseToTerm);
        AddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get selected course details
                String Title = selectedCourse.getCourseName();
                Integer CourseID = selectedCourse.getCourseID();
                Integer MentorID = selectedCourse.getMentorID();
                CourseStatus Status = selectedCourse.getCourseStatus();
                StartDateValue = selectedCourse.getStartDate();
                EndDateValue = selectedCourse.getEndDate();

                // Get database instance and DAOs
                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);
                CourseDAO courseDAO = db.courseDAO();
                TermDAO termDAO = db.termDAO();

                // Create new course and insert into database
                Course course = new Course(TermID, MentorID, Title, StartDateValue, EndDateValue, Status);
                courseDAO.insert(course);

                // Start TermViewActivity with term details
                Intent intent = new Intent(TermAddCourseActivity.this, TermViewActivity.class);
                intent.putExtra("TermID", TermID);
                intent.putExtra("Title", termDAO.getTermName(TermID));
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());

                startActivity(intent);
            }
        });
    }
}
