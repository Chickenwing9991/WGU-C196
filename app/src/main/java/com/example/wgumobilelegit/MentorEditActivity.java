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


public class MentorEditActivity extends Activity {
    public String MentorEmail;
    public String MentorPhone;
    public String MentorTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_details_add);


        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        Integer MentorID = intent.getIntExtra("MentorID", 1);
        String Title = intent.getStringExtra("Title");
        String Email = intent.getStringExtra("Email");
        String Phone = intent.getStringExtra("Phone");


        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.editMentorName);
        title.setText(Title);

        TextView email = findViewById(R.id.editMentorEmail);
        email.setText(String.valueOf(Email));

        TextView phone = findViewById(R.id.editMentorPhone);
        phone.setText(String.valueOf(Phone));

        Button backButton = findViewById(R.id.AddMentorBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MentorEditActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });


        Button SaveMentor = findViewById(R.id.SaveMentor);
        SaveMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTitle = findViewById(R.id.editMentorName);
                String Title = String.valueOf(editTitle.getText());

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                MentorDAO mentorDAO = db.mentorDAO();

                Mentor mentor = new Mentor(MentorID, Title, Email, Phone); // your mentor object
                mentorDAO.update(mentor);

                Intent intent = new Intent(MentorEditActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });
    }
}
