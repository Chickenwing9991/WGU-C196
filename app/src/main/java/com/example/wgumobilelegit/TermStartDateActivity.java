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

// This class is used to set the start date of a term
public class TermStartDateActivity extends Activity {
    public long selectedDateMillis;

    @Override
    // This method is called when the activity is starting
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details_add_start);

        // Initialize the CalendarView and Button
        CalendarView calendarView = findViewById(R.id.term_details_startDate);
        Button SaveButton = findViewById(R.id.term_details_StartDateOK);

        // Set an OnClickListener for the SaveButton
        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create a new intent to start the TermAddActivity
                Intent intent = new Intent(TermStartDateActivity.this, TermAddActivity.class);

                // Add extra data to the intent
                intent.putExtra("Date", "Start");
                intent.putExtra("selectedDateMillis", selectedDateMillis);

                // Set the result to be returned to the parent activity and finish this activity
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        // Set an OnDateChangeListener for the calendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Create a new Calendar instance and set the date to the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                selectedDateMillis = calendar.getTimeInMillis();
            }
        });
    }
}
