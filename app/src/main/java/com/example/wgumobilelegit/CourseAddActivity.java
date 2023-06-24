package com.example.wgumobilelegit;

// Importing necessary libraries and classes
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.wgumobilelegit.Objects.Course;
import com.example.wgumobilelegit.Objects.CourseStatus;
import com.example.wgumobilelegit.Objects.Note;
import com.example.wgumobilelegit.Objects.CourseStatusSpinnerItem;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.NoteDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Main class for adding a course
public class CourseAddActivity extends Activity {

    // Variables to store course details
    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String CourseTitle;
    public CourseStatus SelectedStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details_add);

        // Setting up the spinner for course status
        Spinner spinner = findViewById(R.id.courseAddStatus);

        List<CourseStatusSpinnerItem> items = new ArrayList<>();
        items.add(new CourseStatusSpinnerItem("In Progress", CourseStatus.InProgress));
        items.add(new CourseStatusSpinnerItem("Completed", CourseStatus.Completed));
        items.add(new CourseStatusSpinnerItem("Dropped", CourseStatus.Dropped));
        items.add(new CourseStatusSpinnerItem("Plan to Take", CourseStatus.PlanToTake));

        ArrayAdapter<CourseStatusSpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Setting up the back button
        Button backButton = findViewById(R.id.AddCourseBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseAddActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the start date button
        Button StartDate = findViewById(R.id.GoCourseDetailsStart);
        StartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseAddActivity.this, CourseStartDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Setting up the end date button
        Button EndDate = findViewById(R.id.GoCourseDetailsEnd);
        EndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseAddActivity.this, CourseEndDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Setting up the spinner item selection listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CourseStatusSpinnerItem Status = (CourseStatusSpinnerItem) parent.getItemAtPosition(position);
                SelectedStatus = Status.getStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });

        // Setting up the save course button
        Button SaveCourse = findViewById(R.id.SaveCourse);
        SaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Getting the course title and note from the user
                EditText editTitle = findViewById(R.id.editCourseTitle);
                String Title = editTitle.getText().toString();

                EditText editNote = findViewById(R.id.editNoteMultiLine);
                String NoteText = editNote.getText().toString();

                // Setting up the database
                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                CourseDAO courseDAO = db.courseDAO();
                NoteDAO noteDAO = db.noteDAO();

                // Creating a new course and note
                Course course = new Course(Title, StartDateValue, EndDateValue, SelectedStatus);
                Long CourseID = courseDAO.insert(course);

                Note note = new Note(Math.toIntExact(CourseID), "Course-"+CourseID, NoteText);
                noteDAO.insert(note);

                // Redirecting to the course list activity
                Intent intent = new Intent(CourseAddActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Getting the date from the intent
        String IsDate = data.getStringExtra("Date");

        if (resultCode == RESULT_OK && Objects.equals(IsDate, "Start")) {
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            // Converting the date to LocalDate
            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            StartDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView StartText = findViewById(R.id.editStartDate);
            StartText.setText(String.valueOf(StartDateValue));
        }
        else{
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            // Converting the date to LocalDate
            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            EndDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView EndText = findViewById(R.id.editEndDate);
            EndText.setText(String.valueOf(EndDateValue));
        }
    }
}
