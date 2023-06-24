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

        // Initialize spinner and EditText for notes
        Spinner spinner = findViewById(R.id.courseAddStatus);
        EditText Notes = findViewById(R.id.editNoteMultiLine);

        // Create list of course status options
        List<CourseStatusSpinnerItem> items = new ArrayList<>();
        items.add(new CourseStatusSpinnerItem("In Progress", CourseStatus.InProgress));
        items.add(new CourseStatusSpinnerItem("Completed", CourseStatus.Completed));
        items.add(new CourseStatusSpinnerItem("Dropped", CourseStatus.Dropped));
        items.add(new CourseStatusSpinnerItem("Plan to Take", CourseStatus.PlanToTake));

        // Set up adapter for spinner
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

        // Set the text of the Notes EditText
        Notes.setText(Note);

        // Set the spinner's selection to the CourseStatus value saved in the database
        int spinnerPosition = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getStatus() == Status) {
                spinnerPosition = i;
                break;
            }
        }
        spinner.setSelection(spinnerPosition);

        // Initialize StartDateValue and EndDateValue
        StartDateValue = StartDateParam;
        EndDateValue = EndDateParam;

        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.editCourseTitle);
        title.setText(Title);

        TextView startDate = findViewById(R.id.editStartDate);
        startDate.setText(String.valueOf(StartDateParam));

        TextView endDate = findViewById(R.id.editEndDate);
        endDate.setText(String.valueOf(EndDateParam));

        // Set up back button to return to CourseListActivity
        Button backButton = findViewById(R.id.AddCourseBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseEditActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });

        // Set up StartDate button to open CourseStartDateActivity
        Button StartDate = findViewById(R.id.GoCourseDetailsStart);
        StartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseEditActivity.this, CourseStartDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Set up EndDate button to open CourseEndDateActivity
        Button EndDate = findViewById(R.id.GoCourseDetailsEnd);
        EndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseEditActivity.this, CourseEndDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Set up SaveCourse button to save changes to course and return to CourseListActivity
        Button SaveCourse = findViewById(R.id.SaveCourse);
        SaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the title and note text from the EditTexts
                EditText editTitle = findViewById(R.id.editCourseTitle);
                String Title = String.valueOf(editTitle.getText());

                EditText editNote = findViewById(R.id.editNoteMultiLine);
                String NoteText = editNote.getText().toString();

                // Get instance of database
                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                // Get DAOs for course and note
                CourseDAO courseDAO = db.courseDAO();

                // Get selected course status
                CourseStatusSpinnerItem Status = (CourseStatusSpinnerItem) spinner.getSelectedItem();
                CourseStatus courseStatus = Status.getStatus();

                // Create course object and update in database
                Course course = new Course(CourseID, Title, StartDateValue, EndDateValue, courseStatus);
                courseDAO.update(course);

                // Get note ID, create note object, and update in database
                NoteDAO noteDAO = db.noteDAO();
                Note NoteID = noteDAO.getNoteId(CourseID);
                Note note = new Note(NoteID.getNoteId(),CourseID, "Course-"+CourseID, NoteText);
                noteDAO.update(note);

                // Return to CourseListActivity
                Intent intent = new Intent(CourseEditActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get the date from the returned Intent
        String IsDate = data.getStringExtra("Date");

        if (resultCode == RESULT_OK && Objects.equals(IsDate, "Start")) {
            // If the date is a start date, update StartDateValue and the start date TextView
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            StartDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView StartText = findViewById(R.id.editStartDate);
            StartText.setText(String.valueOf(StartDateValue));
        }
        else{
            // If the date is an end date, update EndDateValue and the end date TextView
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            EndDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView EndText = findViewById(R.id.editEndDate);
            EndText.setText(String.valueOf(EndDateValue));
        }
    }
}
