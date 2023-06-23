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

public class AssessmentDueDateActivity extends Activity {

    public long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_details_add_due);

        CalendarView calendarView = findViewById(R.id.assessment_details_dueDate);
        Button SaveButton = findViewById(R.id.assessment_details_DueDateOK);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(AssessmentDueDateActivity.this, AssessmentAddActivity.class);

                intent.putExtra("Date", "End");
                intent.putExtra("selectedDateMillis", selectedDateMillis);

                setResult(RESULT_OK, intent);
                finish();

                Log.d("Troubleshooting", String.valueOf(selectedDateMillis));
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Note: the month is 0-based
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                selectedDateMillis = calendar.getTimeInMillis();

                Log.d("Troubleshooting", String.valueOf(selectedDateMillis));
            }
        });

    }
}
