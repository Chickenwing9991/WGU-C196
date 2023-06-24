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

import com.example.wgumobilelegit.Adapters.MentorAdapter;
import com.example.wgumobilelegit.Objects.Mentor;
import com.example.wgumobilelegit.dao.MentorDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.util.List;

// This class is responsible for displaying the list of mentors
public class MentorListActivity extends Activity implements MentorAdapter.OnMentorSelectedListener {

    private Mentor selectedMentor;

    // This method is called when a mentor is selected from the list
    @Override
    public void onMentorSelected(Mentor selectedMentor) {
        this.selectedMentor = selectedMentor;
    }

    // This method is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_list);

        // Setting up the RecyclerView for displaying the list of mentors
        RecyclerView recyclerView = findViewById(R.id.MentorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting the database instance and the MentorDAO
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        MentorDAO mentorDAO = db.mentorDAO();

        // Fetching all the mentors from the database
        final List<Mentor> mentors = mentorDAO.getAllMentors();

        // Setting up the adapter for the RecyclerView
        MentorAdapter mentorAdapter = new MentorAdapter(mentors, this);
        recyclerView.setAdapter(mentorAdapter);

        // Setting up the back button
        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MentorListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the add mentor button
        Button AddButton = findViewById(R.id.AddMentor);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MentorListActivity.this, MentorAddActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the view mentor button
        Button ViewButton = findViewById(R.id.viewMentor);
        ViewButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              Intent intent = new Intent(MentorListActivity.this, MentorViewActivity.class);

              if (selectedMentor != null) {
                  Integer mentorID = selectedMentor.getId();
                  String title = selectedMentor.getName() != null ? selectedMentor.getName().toString() : null;
                  String email = selectedMentor.getEmail() != null ? selectedMentor.getEmail().toString() : null;
                  String phone = selectedMentor.getPhone() != null ? selectedMentor.getPhone().toString() : null;

                  intent.putExtra("MentorID", mentorID);
                  intent.putExtra("Title", title);
                  intent.putExtra("Email", email);
                  intent.putExtra("Phone", phone);

                  startActivity(intent);
              } else {
                  Toast.makeText(MentorListActivity.this, "Please select a mentor to edit", Toast.LENGTH_SHORT).show();
              }
          }
      });

        // Setting up the edit mentor button
        Button EditButton = findViewById(R.id.EditMentor);
        EditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MentorListActivity.this, MentorEditActivity.class);

                if (selectedMentor != null) {
                    Integer mentorID = selectedMentor.getId();
                    String title = selectedMentor.getName() != null ? selectedMentor.getName().toString() : null;
                    String email = selectedMentor.getEmail() != null ? selectedMentor.getEmail().toString() : null;
                    String phone = selectedMentor.getPhone() != null ? selectedMentor.getPhone().toString() : null;

                    intent.putExtra("MentorID", mentorID);
                    intent.putExtra("Title", title);
                    intent.putExtra("Email", email);
                    intent.putExtra("Phone", phone);

                    startActivity(intent);
                } else {
                    Toast.makeText(MentorListActivity.this, "Please select a mentor to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setting up the delete mentor button
        Button DeleteButton = findViewById(R.id.DeleteMentor);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Deleting the selected mentor from the database
                mentorDAO.delete(selectedMentor);

                // Refreshing the list of mentors after deletion
                final List<Mentor> mentors = mentorDAO.getAllMentors();

                MentorAdapter mentorAdapter = new MentorAdapter(mentors, MentorListActivity.this);
                recyclerView.setAdapter(mentorAdapter);
            }
        });

        // Starting a new thread to fetch all the mentors from the database
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Mentor> mentors = mentorDAO.getAllMentors();

                // Updating the RecyclerView on the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MentorAdapter mentorAdapter = new MentorAdapter(mentors, MentorListActivity.this);
                        recyclerView.setAdapter(mentorAdapter);
                    }
                });
            }
        }).start();

    }
}
