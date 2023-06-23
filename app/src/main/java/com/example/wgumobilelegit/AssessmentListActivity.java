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

public class AssessmentListActivity extends Activity implements AssessmentAdapter.OnAssessmentSelectedListener {

    private Assessment selectedAssessment;

    @Override
    public void onAssessmentSelected(Assessment selectedAssessment) {
        // This method will be called when an item is selected
        this.selectedAssessment = selectedAssessment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_list);

        RecyclerView recyclerView = findViewById(R.id.AssessmentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        AssessmentDAO assessmentDAO = db.AssessmentDAO();

        final List<Assessment> assessments = assessmentDAO.getAllAssessments();

        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, this); // pass 'this' as the listener
        recyclerView.setAdapter(assessmentAdapter);

        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(AssessmentListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button AddButton = findViewById(R.id.AddAssessment);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentAddActivity.class);
                startActivity(intent);
            }
        });

        Button ViewButton = findViewById(R.id.viewAssessment);
        ViewButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              // Code here executes on main thread after user presses button
              Intent intent = new Intent(AssessmentListActivity.this, AssessmentViewActivity.class);

              if (selectedAssessment != null) {
                  Integer assessmentID = selectedAssessment.getAssessmentId();
                  String title = selectedAssessment.getAssessmentName() != null ? selectedAssessment.getAssessmentName().toString() : null;
                  String dueDate = selectedAssessment.getDueDate() != null ? selectedAssessment.getDueDate().toString() : null;
                  AssessmentType Type = selectedAssessment.getAssessmenType();

                  Log.d("Troubleshooting", "Date"+dueDate);


                  intent.putExtra("AssessmentID", assessmentID);
                  intent.putExtra("Title", title);
                  intent.putExtra("DueDate", dueDate);
                  intent.putExtra("Type", Type.toString());

                  startActivity(intent);
              } else {
                  // Handle the case where no assessment is selected
                  // For example, show a Toast message
                  Toast.makeText(AssessmentListActivity.this, "Please select a assessment to edit", Toast.LENGTH_SHORT).show();
              }
          }
      });


        Button EditButton = findViewById(R.id.EditAssessment);
        EditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentEditActivity.class);

                if (selectedAssessment != null) {
                    Integer assessmentID = selectedAssessment.getAssessmentId();
                    String title = selectedAssessment.getAssessmentName() != null ? selectedAssessment.getAssessmentName().toString() : null;
                    String dueDate = selectedAssessment.getDueDate() != null ? selectedAssessment.getDueDate().toString() : null;
                    AssessmentType Type = selectedAssessment.getAssessmenType();

                    Log.d("Troubleshooting", "Date"+dueDate);


                    intent.putExtra("AssessmentID", assessmentID);
                    intent.putExtra("Title", title);
                    intent.putExtra("DueDate", dueDate);
                    intent.putExtra("Type", Type.toString());

                    startActivity(intent);
                } else {
                    // Handle the case where no assessment is selected
                    // For example, show a Toast message
                    Toast.makeText(AssessmentListActivity.this, "Please select a assessment to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button DeleteButton = findViewById(R.id.DeleteAssessment);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                assessmentDAO.delete(selectedAssessment);

                final List<Assessment> assessments = assessmentDAO.getAllAssessments();

                AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, AssessmentListActivity.this); // pass 'this' as the listener
                recyclerView.setAdapter(assessmentAdapter);
            }
        });


        // Start a new thread for database operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Retrieve all the assessments from the Room database
                final List<Assessment> assessments = assessmentDAO.getAllAssessments();

                // Run the adapter setting on the UI thread since it affects the UI
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
