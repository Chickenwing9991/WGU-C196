package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class CourseEditActivity extends Activity {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String CourseTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details_add);

        Spinner spinner = findViewById(R.id.courseAddStatus);
        EditText Notes = findViewById(R.id.editNoteMultiLine);

        List<CourseStatusSpinnerItem> items = new ArrayList<>();
        items.add(new CourseStatusSpinnerItem("In Progress", CourseStatus.InProgress));
        items.add(new CourseStatusSpinnerItem("Completed", CourseStatus.Completed));
        items.add(new CourseStatusSpinnerItem("Dropped", CourseStatus.Dropped));
        items.add(new CourseStatusSpinnerItem("Plan to Take", CourseStatus.PlanToTake));

        ArrayAdapter<CourseStatusSpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        Integer CourseID = intent.getIntExtra("CourseID", 1);
        String Title = intent.getStringExtra("Title");
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));
        CourseStatus Status = CourseStatus.fromString(intent.getStringExtra("Status"));
        String Note = intent.getStringExtra("Note");

        Notes.setText(Note);

        Log.d("Troubleshooting", "EditStatus " +String.valueOf(intent.getStringExtra("Status")));
        // Set the spinner's selection to the CourseStatus value saved in the database
        int spinnerPosition = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getStatus() == Status) {
                spinnerPosition = i;
                break;
            }
        }
        spinner.setSelection(spinnerPosition);

        StartDateValue = StartDateParam; // Initialize StartDateValue
        EndDateValue = EndDateParam;

        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.editCourseTitle);
        title.setText(Title);

        TextView startDate = findViewById(R.id.editStartDate);
        startDate.setText(String.valueOf(StartDateParam));

        TextView endDate = findViewById(R.id.editEndDate);
        endDate.setText(String.valueOf(EndDateParam));

        Button backButton = findViewById(R.id.AddCourseBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseEditActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });


        Button StartDate = findViewById(R.id.GoCourseDetailsStart);
        StartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseEditActivity.this, CourseStartDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button EndDate = findViewById(R.id.GoCourseDetailsEnd);
        EndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseEditActivity.this, CourseEndDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        Button SaveCourse = findViewById(R.id.SaveCourse);
        SaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTitle = findViewById(R.id.editCourseTitle);
                String Title = String.valueOf(editTitle.getText());

                EditText editNote = findViewById(R.id.editNoteMultiLine);
                String NoteText = editNote.getText().toString();

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                CourseDAO courseDAO = db.courseDAO();

                CourseStatusSpinnerItem Status = (CourseStatusSpinnerItem) spinner.getSelectedItem(); // This will get you the underlying value such as "IN_PROGRESS"
                CourseStatus courseStatus = Status.getStatus();

                Course course = new Course(CourseID, Title, StartDateValue, EndDateValue, courseStatus); // your course object
                courseDAO.update(course);

                NoteDAO noteDAO = db.noteDAO();
                Note NoteID = noteDAO.getNoteId(CourseID);
                Log.d("Troubleshooting", "Note Text: "+String.valueOf(NoteText));
                Note note = new Note(NoteID.getNoteId(),CourseID, "Course-"+CourseID, NoteText);
                noteDAO.update(note);

                Intent intent = new Intent(CourseEditActivity.this, CourseListActivity.class);
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
