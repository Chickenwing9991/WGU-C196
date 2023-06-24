package com.example.wgumobilelegit;

// Import necessary libraries
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wgumobilelegit.Adapters.MentorAdapter;
import com.example.wgumobilelegit.Objects.Mentor;
import com.example.wgumobilelegit.dao.MentorDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.LocalDate;

// Define the MentorViewActivity class
public class MentorViewActivity extends Activity implements MentorAdapter.OnMentorSelectedListener {

    // Declare class variables
    public LocalDate StartDateValue;
    public LocalDate DueDateValue;
    public String MentorTitle;
    public Mentor selectedMentor;

    // Override the onMentorSelected method from the MentorAdapter interface
    @Override
    public void onMentorSelected(Mentor selectedMentor) {
        this.selectedMentor = selectedMentor;
    }

    // Override the onCreate method from the Activity class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_details);

        // Get database instance
        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getDbInstance(context);

        // Create a MentorDAO object
        MentorDAO mentorDAO = db.mentorDAO();

        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        int MentorID = intent.getIntExtra("MentorID", 1);
        String Title = intent.getStringExtra("Title");
        String Email = intent.getStringExtra("Email");
        String Phone = intent.getStringExtra("Phone");

        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.detailMentorName);
        title.setText(Title);

        TextView email = findViewById(R.id.mentorDetailsEmailValue);
        email.setText(Email);

        TextView phone = findViewById(R.id.mentorDetailsPhoneValue);
        phone.setText(Phone);

        // Set up the back button and its click listener
        Button backButton = findViewById(R.id.detailBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Start the MentorListActivity when the back button is clicked
                Intent intent = new Intent(MentorViewActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });

    }
}
