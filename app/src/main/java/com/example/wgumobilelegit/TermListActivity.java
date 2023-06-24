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

// This class is responsible for displaying the list of terms
public class TermListActivity extends Activity implements TermAdapter.OnTermSelectedListener {

    private Term selectedTerm;

    // This method is called when a term is selected from the list
    @Override
    public void onTermSelected(Term selectedTerm) {
        this.selectedTerm = selectedTerm;
    }

    // This method is called when the activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_list);

        // Setting up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.TermList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting instances of the database and DAOs
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        TermDAO termDAO = db.termDAO();
        CourseDAO courseDAO = db.courseDAO();

        // Fetching all terms from the database
        final List<Term> terms = termDAO.getAllTerms();

        // Setting up the adapter for the RecyclerView
        TermAdapter termAdapter = new TermAdapter(terms, this);
        recyclerView.setAdapter(termAdapter);

        // Setting up the back button
        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TermListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the add term button
        Button AddButton = findViewById(R.id.AddTerm);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TermListActivity.this, TermAddActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the view term button
        Button ViewButton = findViewById(R.id.viewTerm);
        ViewButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
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
                  Toast.makeText(TermListActivity.this, "Please select a term to edit", Toast.LENGTH_SHORT).show();
              }
          }
      });

        // Setting up the edit term button
        Button EditButton = findViewById(R.id.EditTerm);
        EditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                    Toast.makeText(TermListActivity.this, "Please select a term to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setting up the delete term button
        Button DeleteButton = findViewById(R.id.DeleteTerm);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(courseDAO.getAssociatedCourses(selectedTerm.getTermID()).size() > 0){
                    Toast.makeText(TermListActivity.this, "Please Delete Associated Courses First", Toast.LENGTH_SHORT).show();
                }
                else{
                    termDAO.delete(selectedTerm);

                    final List<Term> terms = termDAO.getAllTerms();

                    TermAdapter termAdapter = new TermAdapter(terms, TermListActivity.this);
                    recyclerView.setAdapter(termAdapter);
                }
            }
        });

        // Starting a new thread to perform database operations
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Term> terms = termDAO.getAllTerms();

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
