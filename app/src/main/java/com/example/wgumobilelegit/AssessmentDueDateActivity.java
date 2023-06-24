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

// This class is used to set the due date for an assessment
public class AssessmentDueDateActivity extends Activity {

    // Variable to store the selected date in milliseconds
    public long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_details_add_due);

        // Initialize the CalendarView and Button
        CalendarView calendarView = findViewById(R.id.assessment_details_dueDate);
        Button SaveButton = findViewById(R.id.assessment_details_DueDateOK);

        // Set an OnClickListener for the SaveButton
        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create a new intent to go back to the AssessmentAddActivity
                Intent intent = new Intent(AssessmentDueDateActivity.this, AssessmentAddActivity.class);

                // Put extra data in the intent
                intent.putExtra("Date", "End");
                intent.putExtra("selectedDateMillis", selectedDateMillis);

                // Set the result of this activity and finish it
                setResult(RESULT_OK, intent);
                finish();

                // Log the selected date for troubleshooting
                Log.d("Selected Date", String.valueOf(selectedDateMillis));
            }
        });

        // Set an OnDateChangeListener for the calendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Create a new Calendar instance and set the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                selectedDateMillis = calendar.getTimeInMillis();

                // Log the selected date for troubleshooting
                Log.d("Selected Date", String.valueOf(selectedDateMillis));
            }
        });

    }
}
