package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.TermAdapter;
import com.example.wgumobilelegit.Objects.Term;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.TermDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.util.List;

public class TermListActivity extends Activity implements TermAdapter.OnTermSelectedListener {

    private Term selectedTerm;

    @Override
    public void onTermSelected(Term selectedTerm) {
        // This method will be called when an item is selected
        this.selectedTerm = selectedTerm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_list);

        RecyclerView recyclerView = findViewById(R.id.TermList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        TermDAO termDAO = db.termDAO();
        CourseDAO courseDAO = db.courseDAO();

        final List<Term> terms = termDAO.getAllTerms();

        TermAdapter termAdapter = new TermAdapter(terms, this); // pass 'this' as the listener
        recyclerView.setAdapter(termAdapter);

        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button AddButton = findViewById(R.id.AddTerm);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermListActivity.this, TermAddActivity.class);
                startActivity(intent);
            }
        });

        Button ViewButton = findViewById(R.id.viewTerm);
        ViewButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              // Code here executes on main thread after user presses button
              Intent intent = new Intent(TermListActivity.this, TermViewActivity.class);

              if (selectedTerm != null) {
                  Integer termID = selectedTerm.getTermID();
                  String title = selectedTerm.getTermName() != null ? selectedTerm.getTermName().toString() : null;
                  String startDate = selectedTerm.getStartDate() != null ? selectedTerm.getStartDate().toString() : null;
                  String endDate = selectedTerm.getEndDate() != null ? selectedTerm.getEndDate().toString() : null;

                  intent.putExtra("TermID", termID);
                  intent.putExtra("Title", title);
                  intent.putExtra("StartDate", startDate);
                  intent.putExtra("EndDate", endDate);

                  startActivity(intent);
              } else {
                  // Handle the case where no term is selected
                  // For example, show a Toast message
                  Toast.makeText(TermListActivity.this, "Please select a term to edit", Toast.LENGTH_SHORT).show();
              }
          }
      });

        Button EditButton = findViewById(R.id.EditTerm);
        EditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermListActivity.this, TermEditActivity.class);

                if (selectedTerm != null) {
                    Integer termID = selectedTerm.getTermID();
                    String title = selectedTerm.getTermName() != null ? selectedTerm.getTermName().toString() : null;
                    String startDate = selectedTerm.getStartDate() != null ? selectedTerm.getStartDate().toString() : null;
                    String endDate = selectedTerm.getEndDate() != null ? selectedTerm.getEndDate().toString() : null;

                    intent.putExtra("TermID", termID);
                    intent.putExtra("Title", title);
                    intent.putExtra("StartDate", startDate);
                    intent.putExtra("EndDate", endDate);

                    startActivity(intent);
                } else {
                    // Handle the case where no term is selected
                    // For example, show a Toast message
                    Toast.makeText(TermListActivity.this, "Please select a term to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button DeleteButton = findViewById(R.id.DeleteTerm);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                if(courseDAO.getAssociatedCourses(selectedTerm.getTermID()).size() > 0){
                    Toast.makeText(TermListActivity.this, "Please Delete Associated Courses First", Toast.LENGTH_SHORT).show();
                }
                else{
                    termDAO.delete(selectedTerm);

                    final List<Term> terms = termDAO.getAllTerms();

                    TermAdapter termAdapter = new TermAdapter(terms, TermListActivity.this); // pass 'this' as the listener
                    recyclerView.setAdapter(termAdapter);
                }
            }
        });


        // Start a new thread for database operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Retrieve all the terms from the Room database
                final List<Term> terms = termDAO.getAllTerms();

                // Run the adapter setting on the UI thread since it affects the UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TermAdapter termAdapter = new TermAdapter(terms, TermListActivity.this);
                        recyclerView.setAdapter(termAdapter);
                    }
                });
            }
        }).start();

    }
}
