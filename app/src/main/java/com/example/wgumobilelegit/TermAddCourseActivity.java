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


public class TermAddCourseActivity extends Activity implements CourseAdapter.OnCourseSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String TermTitle;
    public Course selectedCourse;

    @Override
    public void onCourseSelected(Course selectedCourse) {
        // This method will be called when an item is selected
        this.selectedCourse = selectedCourse;
        Log.d("Troubleshooting", "onCourseSelected: " + selectedCourse.getCourseName() + ", Status: " + selectedCourse.getCourseStatus());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_course_selection);

        Intent intent = getIntent();
        int TermID = intent.getIntExtra("TermID", 1);

        RecyclerView recyclerView = findViewById(R.id.TermCourseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        CourseDAO courseDAO = db.courseDAO();

        final List<Course> courses = courseDAO.getAllCourses();

        CourseAdapter courseAdapter = new CourseAdapter(courses, this); // pass 'this' as the listener
        recyclerView.setAdapter(courseAdapter);

        Button backButton = findViewById(R.id.AddTermCourseBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermAddCourseActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });


        Button AddCourse = findViewById(R.id.AddCourseToTerm);
        AddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Title = selectedCourse.getCourseName();
                Integer CourseID = selectedCourse.getCourseID();
                CourseStatus Status = selectedCourse.getCourseStatus();
                Log.d("Troubleshooting", "Status "+String.valueOf(Status));
                StartDateValue = selectedCourse.getStartDate();
                EndDateValue = selectedCourse.getEndDate();

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                CourseDAO courseDAO = db.courseDAO();
                TermDAO termDAO = db.termDAO();

                Course course = new Course(CourseID, TermID, Title, StartDateValue, EndDateValue, Status); // your course object
                courseDAO.update(course);

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
