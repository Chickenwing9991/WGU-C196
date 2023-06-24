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

import com.example.wgumobilelegit.Objects.Mentor;
import com.example.wgumobilelegit.dao.MentorDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

// This class is used to edit the details of a mentor
public class MentorEditActivity extends Activity {
    public String MentorEmail;
    public String MentorPhone;
    public String MentorTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_details_add);

        // Extracting the intent that started this activity
        Intent intent = getIntent();
        Integer MentorID = intent.getIntExtra("MentorID", 1);
        String Title = intent.getStringExtra("Title");
        String Email = intent.getStringExtra("Email");
        String Phone = intent.getStringExtra("Phone");

        // Setting the extracted strings as texts for the layout's TextViews
        TextView title = findViewById(R.id.editMentorName);
        title.setText(Title);

        TextView email = findViewById(R.id.editMentorEmail);
        email.setText(String.valueOf(Email));

        TextView phone = findViewById(R.id.editMentorPhone);
        phone.setText(String.valueOf(Phone));

        // Setting up the back button to return to the MentorListActivity
        Button backButton = findViewById(R.id.AddMentorBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MentorEditActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the save button to update the mentor details in the database
        Button SaveMentor = findViewById(R.id.SaveMentor);
        SaveMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTitle = findViewById(R.id.editMentorName);
                String Title = String.valueOf(editTitle.getText());

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                MentorDAO mentorDAO = db.mentorDAO();

                Mentor mentor = new Mentor(MentorID, Title, Phone, Email); // Creating a mentor object
                mentorDAO.update(mentor); // Updating the mentor details in the database

                Intent intent = new Intent(MentorEditActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });
    }
}
