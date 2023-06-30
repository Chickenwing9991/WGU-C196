package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.AssessmentAdapter;
import com.example.wgumobilelegit.Objects.Assessment;
import com.example.wgumobilelegit.Objects.AssessmentType;
import com.example.wgumobilelegit.Objects.Note;
import com.example.wgumobilelegit.dao.AssessmentDAO;
import com.example.wgumobilelegit.dao.NoteDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.util.List;

// This class is responsible for displaying the list of assessments
public class AssessmentListActivity extends Activity implements AssessmentAdapter.OnAssessmentSelectedListener {

    private Assessment selectedAssessment;

    // This method is called when an assessment is selected from the list
    @Override
    public void onAssessmentSelected(Assessment selectedAssessment) {
        this.selectedAssessment = selectedAssessment;
    }

    // This method is called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_list);

        // Setting up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.AssessmentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting the database instance and the DAO
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        AssessmentDAO assessmentDAO = db.AssessmentDAO();

        // Fetching all assessments from the database
        final List<Assessment> assessments = assessmentDAO.getAllAssessments();

        // Setting up the adapter for the RecyclerView
        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, this);
        recyclerView.setAdapter(assessmentAdapter);

        // Setting up the back button
        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the add button
        Button AddButton = findViewById(R.id.AddAssessment);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentAddActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the view button
        Button ViewButton = findViewById(R.id.viewAssessment);
        ViewButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              Intent intent = new Intent(AssessmentListActivity.this, AssessmentViewActivity.class);

              if (selectedAssessment != null) {
                  Integer assessmentID = selectedAssessment.getAssessmentId();
                  String title = selectedAssessment.getAssessmentName() != null ? selectedAssessment.getAssessmentName().toString() : null;
                  String dueDate = selectedAssessment.getDueDate() != null ? selectedAssessment.getDueDate().toString() : null;
                  String startDate = selectedAssessment.getStartDate() != null ? selectedAssessment.getStartDate().toString() : null;
                  AssessmentType Type = selectedAssessment.getAssessmenType();

                  intent.putExtra("AssessmentID", assessmentID);
                  intent.putExtra("Title", title);
                  intent.putExtra("DueDate", dueDate);
                  intent.putExtra("StartDate", startDate);
                  intent.putExtra("Type", Type.toString());

                  startActivity(intent);
              } else {
                  Toast.makeText(AssessmentListActivity.this, "Please select a assessment to edit", Toast.LENGTH_SHORT).show();
              }
          }
      });

        // Setting up the edit button
        Button EditButton = findViewById(R.id.EditAssessment);
        EditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentEditActivity.class);

                if (selectedAssessment != null) {
                    Integer assessmentID = selectedAssessment.getAssessmentId();
                    String title = selectedAssessment.getAssessmentName() != null ? selectedAssessment.getAssessmentName().toString() : null;
                    String dueDate = selectedAssessment.getDueDate() != null ? selectedAssessment.getDueDate().toString() : null;
                    String StartDate = selectedAssessment.getStartDate() != null ? selectedAssessment.getStartDate().toString() : null;
                    AssessmentType Type = selectedAssessment.getAssessmenType();

                    intent.putExtra("AssessmentID", assessmentID);
                    intent.putExtra("Title", title);
                    intent.putExtra("StartDate", StartDate);
                    intent.putExtra("DueDate", dueDate);
                    intent.putExtra("Type", Type.toString());

                    startActivity(intent);
                } else {
                    Toast.makeText(AssessmentListActivity.this, "Please select a assessment to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setting up the delete button
        Button DeleteButton = findViewById(R.id.DeleteAssessment);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Deleting the selected assessment from the database
                assessmentDAO.delete(selectedAssessment);

                // Refreshing the list of assessments
                final List<Assessment> assessments = assessmentDAO.getAllAssessments();

                // Updating the RecyclerView adapter
                AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, AssessmentListActivity.this);
                recyclerView.setAdapter(assessmentAdapter);
            }
        });

        // Starting a new thread to fetch all assessments from the database
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Assessment> assessments = assessmentDAO.getAllAssessments();

                // Updating the RecyclerView adapter on the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, AssessmentListActivity.this);
                        recyclerView.setAdapter(assessmentAdapter);
                    }
                });
            }
        }).start();

    }
}
