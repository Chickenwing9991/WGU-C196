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

public class TermStartDateActivity extends Activity {
    public long selectedDateMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details_add_start);

        CalendarView calendarView = findViewById(R.id.term_details_startDate);
        Button SaveButton = findViewById(R.id.term_details_StartDateOK);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermStartDateActivity.this, TermAddActivity.class);

                intent.putExtra("Date", "Start");
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
