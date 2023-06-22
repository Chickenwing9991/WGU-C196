package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.wgumobilelegit.Objects.Term;
import com.example.wgumobilelegit.dao.TermDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;


public class TermEditActivity extends Activity {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String TermTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details_add);

        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        int TermID = intent.getIntExtra("TermID", 1);
        String Title = intent.getStringExtra("Title");
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));

        StartDateValue = StartDateParam; // Initialize StartDateValue
        EndDateValue = EndDateParam;

        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.editTermTitle);
        title.setText(Title);

        TextView startDate = findViewById(R.id.editStartDate);
        startDate.setText(String.valueOf(StartDateParam));

        TextView endDate = findViewById(R.id.editEndDate);
        endDate.setText(String.valueOf(EndDateParam));

        Button backButton = findViewById(R.id.AddTermBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermEditActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });


        Button StartDate = findViewById(R.id.GoTermDetailsStart);
        StartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermEditActivity.this, TermStartDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        Button EndDate = findViewById(R.id.GoTermDetailsEnd);
        EndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermEditActivity.this, TermEndDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        Button SaveTerm = findViewById(R.id.SaveTerm);
        SaveTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTitle = findViewById(R.id.editTermTitle);
                String Title = editTitle.getText().toString();

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                TermDAO termDAO = db.termDAO();

                Term term = new Term(TermID, Title, StartDateValue, EndDateValue); // your term object
                termDAO.update(term);

                Intent intent = new Intent(TermEditActivity.this, TermListActivity.class);
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
