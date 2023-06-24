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

// This class is responsible for handling the course start date activity
public class CourseStartDateActivity extends Activity {
    // Variable to store the selected date in milliseconds
    public long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the course details add start layout
        setContentView(R.layout.course_details_add_start);

        // Initialize the calendar view and save button
        CalendarView calendarView = findViewById(R.id.course_details_startDate);
        Button SaveButton = findViewById(R.id.course_details_StartDateOK);

        // Set an onClick listener for the save button
        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create a new intent for the CourseAddActivity
                Intent intent = new Intent(CourseStartDateActivity.this, CourseAddActivity.class);

                // Add the date type and selected date to the intent
                intent.putExtra("Date", "Start");
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
                // Store the selected date in milliseconds
                selectedDateMillis = calendar.getTimeInMillis();
            }
        });
    }
}
