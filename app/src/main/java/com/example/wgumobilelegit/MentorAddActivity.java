package com.example.wgumobilelegit;

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


public class MentorAddActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_details_add);

        Button backButton = findViewById(R.id.AddMentorBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MentorAddActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });


        Button SaveMentor = findViewById(R.id.SaveMentor);
        SaveMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTitle = findViewById(R.id.editMentorName);
                String Title = editTitle.getText().toString();

                EditText editEmail = findViewById(R.id.editMentorEmail);
                String Email = editEmail.getText().toString();

                EditText editPhone = findViewById(R.id.editMentorPhone);
                String Phone = editPhone.getText().toString();

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                MentorDAO mentorDAO = db.mentorDAO();

                Mentor mentor = new Mentor(Title,Email, Phone); // your mentor object
                mentorDAO.insert(mentor);

                Intent intent = new Intent(MentorAddActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });
    }
}
