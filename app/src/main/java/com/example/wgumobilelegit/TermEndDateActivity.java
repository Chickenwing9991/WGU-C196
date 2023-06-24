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

// This class is used to set the end date for a term
public class TermEndDateActivity extends Activity {

    // Variable to store the selected date in milliseconds
    public long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details_add_end);

        // Initialize the CalendarView and Button
        CalendarView calendarView = findViewById(R.id.term_details_endDate);
        Button SaveButton = findViewById(R.id.term_details_EndDateOK);

        // Set an OnClickListener for the SaveButton
        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create a new Intent to return the selected date to the TermAddActivity
                Intent intent = new Intent(TermEndDateActivity.this, TermAddActivity.class);

                // Add the date type and selected date to the Intent
                intent.putExtra("Date", "End");
                intent.putExtra("selectedDateMillis", selectedDateMillis);

                // Set the result of the Activity and finish
                setResult(RESULT_OK, intent);
                finish();

                // Log the selected date for debugging purposes
                Log.d("Selected Date", String.valueOf(selectedDateMillis));
            }
        });

        // Set an OnDateChangeListener for the CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Create a new Calendar and set the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                selectedDateMillis = calendar.getTimeInMillis();

                // Log the selected date for debugging purposes
                Log.d("Selected Date", String.valueOf(selectedDateMillis));
            }
        });

    }
}
