package com.example.wgumobilelegit;

// Import necessary libraries
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

// Import necessary classes
import com.example.wgumobilelegit.Objects.Mentor;
import com.example.wgumobilelegit.dao.MentorDAO;
import com.example.wgumobilelegit.dao.NoteDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Define MentorAddActivity class
public class MentorAddActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_details_add);

        // Initialize back button and set its click listener
        Button backButton = findViewById(R.id.AddMentorBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create intent to start MentorListActivity
                Intent intent = new Intent(MentorAddActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });

        // Initialize save button and set its click listener
        Button SaveMentor = findViewById(R.id.SaveMentor);
        SaveMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get mentor details from input fields
                EditText editTitle = findViewById(R.id.editMentorName);
                String Title = editTitle.getText().toString();

                EditText editEmail = findViewById(R.id.editMentorEmail);
                String Email = editEmail.getText().toString();

                EditText editPhone = findViewById(R.id.editMentorPhone);
                String Phone = editPhone.getText().toString();

                // Get application context and database instance
                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                // Get MentorDAO instance
                MentorDAO mentorDAO = db.mentorDAO();

                // Create new mentor object and insert it into the database
                Mentor mentor = new Mentor(Title,Phone, Email);
                mentorDAO.insert(mentor);

                // Create intent to start MentorListActivity
                Intent intent = new Intent(MentorAddActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });
    }
}
