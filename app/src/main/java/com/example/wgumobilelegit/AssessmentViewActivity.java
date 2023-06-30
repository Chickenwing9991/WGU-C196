package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.AssessmentAdapter;
import com.example.wgumobilelegit.Objects.Assessment;
import com.example.wgumobilelegit.Objects.AssessmentType;
import com.example.wgumobilelegit.dao.AssessmentDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

// This class is responsible for the Assessment View Activity
public class AssessmentViewActivity extends Activity implements AssessmentAdapter.OnAssessmentSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate DueDateValue;
    public String AssessmentTitle;
    public Assessment selectedAssessment;

    // This method is called when an item is selected
    @Override
    public void onAssessmentSelected(Assessment selectedAssessment) {
        this.selectedAssessment = selectedAssessment;
    }

    // This method is called when the activity is starting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_details);

        // Get database access
        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getDbInstance(context);

        AssessmentDAO assessmentDAO = db.AssessmentDAO();

        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        int AssessmentID = intent.getIntExtra("AssessmentID", 1);
        String Title = intent.getStringExtra("Title");
        LocalDate DueDateParam = LocalDate.parse(intent.getStringExtra("DueDate"));
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        AssessmentType Type = AssessmentType.fromString(intent.getStringExtra("Type"));

        DueDateValue = DueDateParam;
        StartDateValue = StartDateParam;

        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.detailAssessmentName);
        title.setText(Title);

        TextView endDate = findViewById(R.id.assessmentDetailsDueValue);
        endDate.setText(String.valueOf(DueDateParam));

        TextView startDate = findViewById(R.id.assessmentDetailsStartValue);
        startDate.setText(String.valueOf(StartDateParam));

        TextView TypeVal = findViewById(R.id.assessmentDetailsTypeValue);
        TypeVal.setText(Type.toString());

        // Set up the back button and its click listener
        Button backButton = findViewById(R.id.detailBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This code executes on main thread after user presses button
                Intent intent = new Intent(AssessmentViewActivity.this, AssessmentListActivity.class);
                startActivity(intent);
            }
        });

    }
}
