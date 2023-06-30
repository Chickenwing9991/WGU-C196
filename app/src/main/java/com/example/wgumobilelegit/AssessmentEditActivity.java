package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// AssessmentEditActivity class
public class AssessmentEditActivity extends Activity {
    public LocalDate DueDateValue;
    public LocalDate StartDateValue;
    public String AssessmentTitle;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_details_add);

        // Initialize spinner
        Spinner spinner = findViewById(R.id.assessmentAddType);

        // Create list of items for spinner
        List<AssessmentTypeSpinnerItem> items = new ArrayList<>();
        items.add(new AssessmentTypeSpinnerItem("Performance Assessment", AssessmentType.Performance));
        items.add(new AssessmentTypeSpinnerItem("Objective Assessment", AssessmentType.Objective));

        // Set up adapter for spinner
        ArrayAdapter<AssessmentTypeSpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get intent and extract data
        Intent intent = getIntent();
        Integer AssessmentID = intent.getIntExtra("AssessmentID", 1);
        String Title = intent.getStringExtra("Title");
        LocalDate DueDateParam = LocalDate.parse(intent.getStringExtra("DueDate"));
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        AssessmentType Type = AssessmentType.fromString(intent.getStringExtra("Type"));

        // Set spinner selection based on AssessmentType
        int spinnerPosition = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getType() == Type) {
                spinnerPosition = i;
                break;
            }
        }
        spinner.setSelection(spinnerPosition);

        DueDateValue = DueDateParam;
        StartDateValue = StartDateParam;

        // Set text for TextViews
        TextView title = findViewById(R.id.editAssessmentTitle);
        title.setText(Title);

        TextView endDate = findViewById(R.id.editDueDate);
        endDate.setText(String.valueOf(DueDateParam));


        TextView startDate = findViewById(R.id.editAssStartDate);
        startDate.setText(String.valueOf(StartDateParam));
        // Set onClickListener for backButton
        Button backButton = findViewById(R.id.AddAssessmentBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentEditActivity.this, AssessmentListActivity.class);
                startActivity(intent);
            }
        });

        // Set onClickListener for DueDate button
        Button DueDate = findViewById(R.id.GoAssessmentDetailsEnd);
        DueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentEditActivity.this, AssessmentDueDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Set onClickListener for StartDate button
        Button StartDate = findViewById(R.id.GoAssessmentDetailsStart);
        DueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentEditActivity.this, AssessmentStartDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        // Set onClickListener for SaveAssessment button
        Button SaveAssessment = findViewById(R.id.SaveAssessment);
        SaveAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get title from EditText
                EditText editTitle = findViewById(R.id.editAssessmentTitle);
                String Title = String.valueOf(editTitle.getText());

                // Get database instance
                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                // Get DAO
                AssessmentDAO assessmentDAO = db.AssessmentDAO();

                // Get selected AssessmentType
                AssessmentTypeSpinnerItem Type = (AssessmentTypeSpinnerItem) spinner.getSelectedItem();
                AssessmentType assessmentType = Type.getType();

                // Create new Assessment object and update in database
                Assessment assessment = new Assessment(AssessmentID, null, Title, assessmentType, DueDateValue, StartDateValue);
                assessmentDAO.update(assessment);

                // Start AssessmentListActivity
                Intent intent = new Intent(AssessmentEditActivity.this, AssessmentListActivity.class);
                startActivity(intent);
            }
        });
    }

    // onActivityResult method
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
            TextView StartText = findViewById(R.id.editAssStartDate);
            StartText.setText(String.valueOf(StartDateValue));
        }
        else{
            long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

            // Convert the selected date to LocalDate
            Instant instant = Instant.ofEpochMilli(selectedDateMillis);
            DueDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TextView EndText = findViewById(R.id.editDueDate);
            EndText.setText(String.valueOf(DueDateValue));
        }
    }
}
