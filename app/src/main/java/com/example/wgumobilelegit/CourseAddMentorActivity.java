package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.MentorAdapter;
import com.example.wgumobilelegit.Objects.Course;
import com.example.wgumobilelegit.Objects.Mentor;
import com.example.wgumobilelegit.Objects.CourseStatus;
import com.example.wgumobilelegit.dao.MentorDAO;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.TermDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.LocalDate;
import java.util.List;

// Activity for adding a mentor to a course
public class CourseAddMentorActivity extends Activity implements MentorAdapter.OnMentorSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String TermTitle;
    public Mentor selectedMentor;

    // Method to handle mentor selection
    @Override
    public void onMentorSelected(Mentor selectedMentor) {
        this.selectedMentor = selectedMentor;
    }

    // Method to handle activity creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_mentor_selection);

        // Get intent and extract data
        Intent intent = getIntent();
        int CourseID = intent.getIntExtra("CourseID", 1);
        String CourseTitle = intent.getStringExtra("Title");
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));
        CourseStatus Status = CourseStatus.fromString(intent.getStringExtra("Status"));
        String Note = intent.getStringExtra("Note");

        // Initialize date values
        StartDateValue = StartDateParam;
        EndDateValue = EndDateParam;

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.CourseMentorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get database instance and DAO
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        MentorDAO mentorDAO = db.mentorDAO();

        // Get all mentors
        final List<Mentor> mentors = mentorDAO.getAllMentors();

        // Set up adapter and attach to RecyclerView
        MentorAdapter mentorAdapter = new MentorAdapter(mentors, this);
        recyclerView.setAdapter(mentorAdapter);

        // Set up back button and its click listener
        Button backButton = findViewById(R.id.AddCourseMentorBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create intent and put extras
                Intent intent = new Intent(CourseAddMentorActivity.this, CourseViewActivity.class);

                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", CourseTitle);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);

                // Start CourseViewActivity
                startActivity(intent);
            }
        });

        // Set up AddMentor button and its click listener
        Button AddMentor = findViewById(R.id.AddMentorToCourse);
        AddMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get DAOs
                TermDAO termDAO = db.termDAO();
                MentorDAO mentorDAO = db.mentorDAO();
                CourseDAO courseDAO = db.courseDAO();

                // Get selected mentor details
                String Title = selectedMentor.getName();
                Integer MentorID = selectedMentor.getId();
                String Email = selectedMentor.getEmail();
                String Phone = selectedMentor.getPhone();

                // Create course object and update in database
                Course course = new Course(CourseID, null, MentorID, CourseTitle, StartDateValue, EndDateValue, Status);
                courseDAO.update(course);

                // Create intent and put extras
                Intent intent = new Intent(CourseAddMentorActivity.this, CourseViewActivity.class);

                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", CourseTitle);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);

                // Start CourseViewActivity
                startActivity(intent);
            }
        });
    }
}
