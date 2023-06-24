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

    @Override
    public void onCourseSelected(Course selectedCourse) {
        // This method will be called when an item is selected
        this.selectedCourse = selectedCourse;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details);

        //Get DB Access
        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getDbInstance(context);

        CourseDAO courseDAO = db.courseDAO();
        /////

        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        int TermID = intent.getIntExtra("TermID", 1);
        Log.d("Troubleshooting", "TermID "+String.valueOf(TermID));
        String Title = intent.getStringExtra("Title");
        Log.d("Troubleshooting", "Status "+String.valueOf(intent.getStringExtra("StartDate")));
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));

        StartDateValue = StartDateParam; // Initialize StartDateValue
        EndDateValue = EndDateParam;

        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.detailTermName);
        title.setText(Title);

        TextView startDate = findViewById(R.id.termDetailsStartValue);
        startDate.setText(String.valueOf(StartDateParam));

        TextView endDate = findViewById(R.id.termDetailsEndValue);
        endDate.setText(String.valueOf(EndDateParam));

        RecyclerView recyclerView = findViewById(R.id.termDetailsCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final List<Course> courses = courseDAO.getAssociatedCourses(TermID);
        Log.d("Troubleshooting", "Courses "+String.valueOf(courses));

        CourseAdapter courseAdapter = new CourseAdapter(courses, this); // pass 'this' as the listener
        recyclerView.setAdapter(courseAdapter);

        Button backButton = findViewById(R.id.detailBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermViewActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });

        Button AddCourse = findViewById(R.id.termDetailsAddCourse);
        AddCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermViewActivity.this, TermAddCourseActivity.class);

                intent.putExtra("TermID", TermID);

                startActivity(intent);
            }
        });

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
}
