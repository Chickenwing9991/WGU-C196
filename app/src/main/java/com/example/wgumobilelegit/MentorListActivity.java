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

public class MentorListActivity extends Activity implements MentorAdapter.OnMentorSelectedListener {

    private Mentor selectedMentor;

    @Override
    public void onMentorSelected(Mentor selectedMentor) {
        // This method will be called when an item is selected
        this.selectedMentor = selectedMentor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_list);

        RecyclerView recyclerView = findViewById(R.id.MentorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        MentorDAO mentorDAO = db.mentorDAO();

        final List<Mentor> mentors = mentorDAO.getAllMentors();

        MentorAdapter mentorAdapter = new MentorAdapter(mentors, this); // pass 'this' as the listener
        recyclerView.setAdapter(mentorAdapter);

        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MentorListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button AddButton = findViewById(R.id.AddMentor);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MentorListActivity.this, MentorAddActivity.class);
                startActivity(intent);
            }
        });

        Button ViewButton = findViewById(R.id.viewMentor);
        ViewButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              // Code here executes on main thread after user presses button
              Intent intent = new Intent(MentorListActivity.this, MentorViewActivity.class);

              if (selectedMentor != null) {
                  Integer mentorID = selectedMentor.getId();
                  String title = selectedMentor.getName() != null ? selectedMentor.getName().toString() : null;
                  String email = selectedMentor.getEmail() != null ? selectedMentor.getEmail().toString() : null;
                  String phone = selectedMentor.getPhone() != null ? selectedMentor.getPhone().toString() : null;

                  Log.d("Troubleshooting", "Date"+phone);


                  intent.putExtra("MentorID", mentorID);
                  intent.putExtra("Title", title);
                  intent.putExtra("Email", email);
                  intent.putExtra("Phone", phone);

                  startActivity(intent);
              } else {
                  // Handle the case where no mentor is selected
                  // For example, show a Toast message
                  Toast.makeText(MentorListActivity.this, "Please select a mentor to edit", Toast.LENGTH_SHORT).show();
              }
          }
      });


        Button EditButton = findViewById(R.id.EditMentor);
        EditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MentorListActivity.this, MentorEditActivity.class);

                if (selectedMentor != null) {
                    Integer mentorID = selectedMentor.getId();
                    String title = selectedMentor.getName() != null ? selectedMentor.getName().toString() : null;
                    String email = selectedMentor.getEmail() != null ? selectedMentor.getEmail().toString() : null;
                    String phone = selectedMentor.getPhone() != null ? selectedMentor.getPhone().toString() : null;

                    Log.d("Troubleshooting", "Date"+phone);


                    intent.putExtra("MentorID", mentorID);
                    intent.putExtra("Title", title);
                    intent.putExtra("Email", email);
                    intent.putExtra("Phone", phone);

                    startActivity(intent);
                } else {
                    // Handle the case where no mentor is selected
                    // For example, show a Toast message
                    Toast.makeText(MentorListActivity.this, "Please select a mentor to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button DeleteButton = findViewById(R.id.DeleteMentor);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                mentorDAO.delete(selectedMentor);

                final List<Mentor> mentors = mentorDAO.getAllMentors();

                MentorAdapter mentorAdapter = new MentorAdapter(mentors, MentorListActivity.this); // pass 'this' as the listener
                recyclerView.setAdapter(mentorAdapter);
            }
        });


        // Start a new thread for database operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Retrieve all the mentors from the Room database
                final List<Mentor> mentors = mentorDAO.getAllMentors();

                // Run the adapter setting on the UI thread since it affects the UI
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
