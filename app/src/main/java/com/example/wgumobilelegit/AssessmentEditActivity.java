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
import com.example.wgumobilelegit.Objects.Note;
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


public class AssessmentEditActivity extends Activity {
    public LocalDate DueDateValue;
    public String AssessmentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_details_add);

        Spinner spinner = findViewById(R.id.assessmentAddType);

        List<AssessmentTypeSpinnerItem> items = new ArrayList<>();
        items.add(new AssessmentTypeSpinnerItem("Test", AssessmentType.Test));
        items.add(new AssessmentTypeSpinnerItem("Project", AssessmentType.Project));
        items.add(new AssessmentTypeSpinnerItem("Paper", AssessmentType.Paper));
        items.add(new AssessmentTypeSpinnerItem("Practical", AssessmentType.Practical));

        ArrayAdapter<AssessmentTypeSpinnerItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        Integer AssessmentID = intent.getIntExtra("AssessmentID", 1);
        String Title = intent.getStringExtra("Title");
        LocalDate DueDateParam = LocalDate.parse(intent.getStringExtra("DueDate"));
        AssessmentType Type = AssessmentType.fromString(intent.getStringExtra("Type"));

        Log.d("Troubleshooting", "EditType " +String.valueOf(intent.getStringExtra("Type")));
        // Set the spinner's selection to the AssessmentType value saved in the database
        int spinnerPosition = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getType() == Type) {
                spinnerPosition = i;
                break;
            }
        }
        spinner.setSelection(spinnerPosition);

        DueDateValue = DueDateParam;

        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.editAssessmentTitle);
        title.setText(Title);

        TextView endDate = findViewById(R.id.editDueDate);
        endDate.setText(String.valueOf(DueDateParam));

        Button backButton = findViewById(R.id.AddAssessmentBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(AssessmentEditActivity.this, AssessmentListActivity.class);
                startActivity(intent);
            }
        });


        Button DueDate = findViewById(R.id.GoAssessmentDetailsEnd);
        DueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(AssessmentEditActivity.this, AssessmentDueDateActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        Button SaveAssessment = findViewById(R.id.SaveAssessment);
        SaveAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTitle = findViewById(R.id.editAssessmentTitle);
                String Title = String.valueOf(editTitle.getText());

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                AssessmentDAO assessmentDAO = db.AssessmentDAO();

                AssessmentTypeSpinnerItem Type = (AssessmentTypeSpinnerItem) spinner.getSelectedItem(); // This will get you the underlying value such as "IN_PROGRESS"
                AssessmentType assessmentType = Type.getType();

                Assessment assessment = new Assessment(AssessmentID, Title, assessmentType, DueDateValue); // your assessment object
                assessmentDAO.update(assessment);

                Intent intent = new Intent(AssessmentEditActivity.this, AssessmentListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String IsDate = data.getStringExtra("Date");
        Log.d("Troubleshooting", String.valueOf(IsDate));

        long selectedDateMillis = data.getLongExtra("selectedDateMillis", 0);

        Instant instant = Instant.ofEpochMilli(selectedDateMillis);
        DueDateValue = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        TextView EndText = findViewById(R.id.editDueDate);
        EndText.setText(String.valueOf(DueDateValue));

        Log.d("Troubleshooting", String.valueOf(DueDateValue));
        Log.d("Troubleshooting", String.valueOf("End"));
        }
    }
