package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import java.util.Calendar;

// This class is responsible for handling the end date of a course
public class CourseEndDateActivity extends Activity {

    // Variable to store the selected date in milliseconds
    public long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details_add_end);

        // Initialize the calendar view and save button
        CalendarView calendarView = findViewById(R.id.course_details_endDate);
        Button SaveButton = findViewById(R.id.course_details_EndDateOK);

        // Set an onClick listener for the save button
        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create a new intent for CourseAddActivity
                Intent intent = new Intent(CourseEndDateActivity.this, CourseAddActivity.class);

                // Add extra data to the intent
                intent.putExtra("Date", "End");
                intent.putExtra("selectedDateMillis", selectedDateMillis);

                // Set the result of the activity and finish it
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Set an onDateChangeListener for the calendar view
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Create a new calendar instance and set the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                selectedDateMillis = calendar.getTimeInMillis();
            }
        });

    }
}
