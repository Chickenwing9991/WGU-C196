package com.example.wgumobilelegit;

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
import com.example.wgumobilelegit.Objects.SpinnerItem;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.NoteDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CourseAddActivity extends Activity {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String CourseTitle;
    public CourseStatus SelectedStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details_add);

        Spinner spinner = findViewById(R.id.courseAddStatus);

        List<SpinnerItem> items = new ArrayList<>();
        items.add(new SpinnerItem("In Progress", CourseStatus.InProgress));
        items.add(new SpinnerItem("Completed", CourseStatus.Completed));
        items.add(new SpinnerItem("Dropped", CourseStatus.Dropped));
        items.add(new SpinnerItem("Plan to Take", CourseStatus.PlanToTake));

        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Button backButton = findViewById(R.id.AddCourseBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseAddActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });


        Button StartDate = findViewById(R.id.GoCourseDetailsStart);
        StartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseAddActivity.this, CourseStartDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button EndDate = findViewById(R.id.GoCourseDetailsEnd);
        EndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseAddActivity.this, CourseEndDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem Status = (SpinnerItem) parent.getItemAtPosition(position); // This will get you the underlying value such as "IN_PROGRESS"
                SelectedStatus = Status.getStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle the case where nothing is selected
            }
        });



        Button SaveCourse = findViewById(R.id.SaveCourse);
        SaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTitle = findViewById(R.id.editCourseTitle);
                String Title = editTitle.getText().toString();

                EditText editNote = findViewById(R.id.editNoteMultiLine);
                String NoteText = editNote.getText().toString();

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                CourseDAO courseDAO = db.courseDAO();
                NoteDAO noteDAO = db.noteDAO();

                Course course = new Course(Title, StartDateValue, EndDateValue, SelectedStatus); // your course object
                Long CourseID = courseDAO.insert(course);

                Note note = new Note(Math.toIntExact(CourseID), "Course-"+CourseID, NoteText);
                noteDAO.insert(note);

                Intent intent = new Intent(CourseAddActivity.this, CourseListActivity.class);
                startActivity(intent);
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
