package com.example.wgumobilelegit;

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


public class MentorViewActivity extends Activity implements MentorAdapter.OnMentorSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate DueDateValue;
    public String MentorTitle;
    public Mentor selectedMentor;

    @Override
    public void onMentorSelected(Mentor selectedMentor) {
        // This method will be called when an item is selected
        this.selectedMentor = selectedMentor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_details);

        //Get DB Access
        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getDbInstance(context);

        MentorDAO mentorDAO = db.mentorDAO();
        /////

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

        Button backButton = findViewById(R.id.detailBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MentorViewActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });

    }
}
