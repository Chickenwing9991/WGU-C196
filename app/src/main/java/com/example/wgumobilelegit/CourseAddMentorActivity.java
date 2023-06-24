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


public class CourseAddMentorActivity extends Activity implements MentorAdapter.OnMentorSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String TermTitle;
    public Mentor selectedMentor;

    @Override
    public void onMentorSelected(Mentor selectedMentor) {
        // This method will be called when an item is selected
        this.selectedMentor = selectedMentor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_mentor_selection);

        Intent intent = getIntent();
        int CourseID = intent.getIntExtra("CourseID", 1);
        String CourseTitle = intent.getStringExtra("Title");
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));
        CourseStatus Status = CourseStatus.fromString(intent.getStringExtra("Status"));
        String Note = intent.getStringExtra("Note");

        StartDateValue = StartDateParam; // Initialize StartDateValue
        EndDateValue = EndDateParam;

        RecyclerView recyclerView = findViewById(R.id.CourseMentorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        MentorDAO mentorDAO = db.mentorDAO();

        final List<Mentor> mentors = mentorDAO.getAllMentors();

        MentorAdapter mentorAdapter = new MentorAdapter(mentors, this); // pass 'this' as the listener
        recyclerView.setAdapter(mentorAdapter);

        Button backButton = findViewById(R.id.AddCourseMentorBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseAddMentorActivity.this, CourseViewActivity.class);

                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", CourseTitle);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);

                startActivity(intent);
            }
        });


        Button AddMentor = findViewById(R.id.AddMentorToCourse);
        AddMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TermDAO termDAO = db.termDAO();

                String Title = selectedMentor.getName();
                Integer MentorID = selectedMentor.getId();
                Log.d("Troubleshoot", "MentorID"+MentorID);
                String Email = selectedMentor.getEmail();
                String Phone = selectedMentor.getPhone();

                Context context = getApplicationContext();
                AppDatabase db = AppDatabase.getDbInstance(context);

                MentorDAO mentorDAO = db.mentorDAO();
                CourseDAO courseDAO = db.courseDAO();

                Course course = new Course(CourseID, null, MentorID, CourseTitle, StartDateValue, EndDateValue, Status); // your mentor object
                courseDAO.update(course);

                Intent intent = new Intent(CourseAddMentorActivity.this, CourseViewActivity.class);

                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", CourseTitle);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);

                startActivity(intent);
            }
        });
    }
}
