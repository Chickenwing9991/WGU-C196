package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
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


// This class is responsible for adding a new term
public class TermAddActivity extends Activity {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String TermTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details_add);

        // Initialize back button and set its click listener
        Button backButton = findViewById(R.id.AddTermBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TermAddActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });

        // Initialize start date button and set its click listener
        Button StartDate = findViewById(R.id.GoTermDetailsStart);
        StartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TermAddActivity.this, TermStartDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Initialize end date button and set its click listener
        Button EndDate = findViewById(R.id.GoTermDetailsEnd);
        EndDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TermAddActivity.this, TermEndDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Initialize save term button and set its click listener
        Button SaveTerm = findViewById(R.id.SaveTerm);
        SaveTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the title from the input field
                EditText editTitle = findViewById(R.id.editTermTitle);
                String Title = editTitle.getText().toString();

                // Get the database instance
                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                // Get the term DAO
                TermDAO termDAO = db.termDAO();

                // Create a new term and insert it into the database
                Term term = new Term(Title, StartDateValue, EndDateValue);
                termDAO.insertNew(term);

                // Start the TermListActivity
                Intent intent = new Intent(TermAddActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String IsDate = data.getStringExtra("Date");

        // Check if the result is for the start date
        if (resultCode == RESULT_OK && Objects.equals(IsDate, "Start")) {
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            // Convert the selected date to LocalDate
            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            StartDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView StartText = findViewById(R.id.editStartDate);
            StartText.setText(String.valueOf(StartDateValue));
        }
        else{
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            // Convert the selected date to LocalDate
            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            EndDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView EndText = findViewById(R.id.editEndDate);
            EndText.setText(String.valueOf(EndDateValue));
        }
    }
}
