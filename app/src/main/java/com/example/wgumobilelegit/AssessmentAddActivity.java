package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.wgumobilelegit.Objects.Assessment;
import com.example.wgumobilelegit.Objects.AssessmentType;
import com.example.wgumobilelegit.Objects.AssessmentTypeSpinnerItem;
import com.example.wgumobilelegit.dao.AssessmentDAO;
import com.example.wgumobilelegit.dao.NoteDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AssessmentAddActivity extends Activity {

    // Define class variables
    public LocalDate StartDateValue;
    public LocalDate DueDateValue;
    public String AssessmentTitle;
    public AssessmentType SelectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_details_add);

        // Initialize spinner
        Spinner spinner = findViewById(R.id.assessmentAddType);

        // Create list of spinner items
        List<AssessmentTypeSpinnerItem> items = new ArrayList<>();
        items.add(new AssessmentTypeSpinnerItem("Performance Assessment", AssessmentType.Performance));
        items.add(new AssessmentTypeSpinnerItem("Objective Assessment", AssessmentType.Objective));

        // Set up spinner adapter
        ArrayAdapter<AssessmentTypeSpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Initialize back button and set click listener
        Button backButton = findViewById(R.id.AddAssessmentBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentAddActivity.this, AssessmentListActivity.class);
                startActivity(intent);
            }
        });

        // Initialize due date button and set click listener
        Button DueDate = findViewById(R.id.GoAssessmentDetailsEnd);
        DueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentAddActivity.this, AssessmentDueDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Set spinner item selected listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AssessmentTypeSpinnerItem Type = (AssessmentTypeSpinnerItem) parent.getItemAtPosition(position);
                SelectedType = Type.getType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
            }
        });

        // Initialize save button and set click listener
        Button SaveAssessment = findViewById(R.id.SaveAssessment);
        SaveAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get title from edit text
                EditText editTitle = findViewById(R.id.editAssessmentTitle);
                String Title = editTitle.getText().toString();

                // Get database instance
                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                // Get DAOs
                AssessmentDAO assessmentDAO = db.AssessmentDAO();
                NoteDAO noteDAO = db.noteDAO();

                // Create new assessment and insert into database
                Assessment assessment = new Assessment(Title,SelectedType, DueDateValue);
                assessmentDAO.insert(assessment);

                // Start AssessmentListActivity
                Intent intent = new Intent(AssessmentAddActivity.this, AssessmentListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get date type from intent extras
        String IsDate = data.getStringExtra("Date");

        if (resultCode == RESULT_OK && Objects.equals(IsDate, "Start")) {
            // Get selected date and convert to LocalDate
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);
            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            StartDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            // Update start date text view
            TextView StartText = findViewById(R.id.editStartDate);
            StartText.setText(String.valueOf(StartDateValue));
        }
        else{
            // Get selected date and convert to LocalDate
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);
            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            DueDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();

            // Update due date text view
            TextView EndText = findViewById(R.id.editDueDate);
            EndText.setText(String.valueOf(DueDateValue));
        }
    }
}
