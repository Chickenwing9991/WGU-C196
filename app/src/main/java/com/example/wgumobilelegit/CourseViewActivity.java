package com.example.wgumobilelegit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.AssessmentAdapter;
import com.example.wgumobilelegit.Adapters.CourseAdapter;
import com.example.wgumobilelegit.Adapters.MentorAdapter;
import com.example.wgumobilelegit.Objects.Assessment;
import com.example.wgumobilelegit.Objects.Course;
import com.example.wgumobilelegit.Objects.CourseStatus;
import com.example.wgumobilelegit.Objects.Mentor;
import com.example.wgumobilelegit.dao.AssessmentDAO;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.MentorDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


public class CourseViewActivity extends Activity implements AssessmentAdapter.OnAssessmentSelectedListener,MentorAdapter.OnMentorSelectedListener {

    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String CourseTitle;
    public Assessment selectedAssessment;
    public Mentor selectedMentor;

    @Override
    public void onAssessmentSelected(Assessment selectedAssessment) {
        // This method will be called when an item is selected
        this.selectedAssessment = selectedAssessment;
        Log.d("TroubleShoot", selectedAssessment.getAssessmentName()+" "+selectedAssessment.getAssessmentId());
    }

    @Override
    public void onMentorSelected(Mentor selectedMentor) {
        // This method will be called when an item in the Mentor list is selected
        // Handle selectedMentor here
        this.selectedMentor = selectedMentor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        TextView Notes = findViewById(R.id.courseDetailsNotesValue);

        //Get DB Access
        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getDbInstance(context);

        CourseDAO courseDAO = db.courseDAO();
        AssessmentDAO assessmentDAO = db.AssessmentDAO();
        MentorDAO mentorDAO = db.mentorDAO();
        /////

        // Get the Intent that started this activity and extract the strings
        Intent intent = getIntent();
        int CourseID = intent.getIntExtra("CourseID", 1);
        String Title = intent.getStringExtra("Title");
        LocalDate StartDateParam = LocalDate.parse(intent.getStringExtra("StartDate"));
        LocalDate EndDateParam = LocalDate.parse(intent.getStringExtra("EndDate"));
        CourseStatus Status = CourseStatus.fromString(intent.getStringExtra("Status"));
        String Note = intent.getStringExtra("Note");

        Notes.setText(Note);

        StartDateValue = StartDateParam; // Initialize StartDateValue
        EndDateValue = EndDateParam;

        // Capture the layout's TextViews and set the strings as their texts
        TextView title = findViewById(R.id.detailCourseName);
        title.setText(Title);

        TextView startDate = findViewById(R.id.courseDetailsStartValue);
        startDate.setText(String.valueOf(StartDateParam));

        TextView endDate = findViewById(R.id.courseDetailsEndValue);
        endDate.setText(String.valueOf(EndDateParam));

        TextView StatusVal = findViewById(R.id.courseDetailsStatusValue);
        StatusVal.setText(Status.toString());

        RecyclerView recyclerView = findViewById(R.id.courseDetailsAssessments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<Assessment> assessments = assessmentDAO.getAssociatedAssessments(CourseID);
        Log.d("TroubleShoot", "CourseID"+CourseID);
        Log.d("TroubleShoot", "Ass List"+assessments);

        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, this); // pass 'this' as the listener
        recyclerView.setAdapter(assessmentAdapter);


        RecyclerView recyclerViewM = findViewById(R.id.courseDetailsMentors);
        recyclerViewM.setLayoutManager(new LinearLayoutManager(this));
        final List<Mentor> mentors = mentorDAO.getAssociatedMentors(CourseID);
        Log.d("TroubleShoot", "Ass List"+assessments);

        MentorAdapter mentorAdapter = new MentorAdapter(mentors, this); // pass 'this' as the listener
        recyclerViewM.setAdapter(mentorAdapter);

        Button backButton = findViewById(R.id.detailBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseViewActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });

        Button AddAssessment = findViewById(R.id.courseDetailsAddAssessment);
        AddAssessment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseViewActivity.this, CourseAddAssessmentActivity.class);

                Log.d("Troubleshoot", "Start Date"+StartDateValue);


                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", Title);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);

                startActivity(intent);
            }
        });

        Button AddMentor = findViewById(R.id.courseDetailsAddMentors);
        AddMentor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseViewActivity.this, CourseAddMentorActivity.class);

                Log.d("Troubleshoot", "Start Date"+StartDateValue);


                intent.putExtra("CourseID", CourseID);
                intent.putExtra("Title", Title);
                intent.putExtra("StartDate", StartDateValue.toString());
                intent.putExtra("EndDate", EndDateValue.toString());
                intent.putExtra("Status", Status.toString());
                intent.putExtra("Note", Note);


                startActivity(intent);
            }
        });

        Button DeleteAssessment = findViewById(R.id.courseDetailsDeleteMentors);
        DeleteAssessment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Course course = new Course(CourseID, selectedMentor.getId(), CourseTitle, StartDateValue, EndDateValue, Status);

                courseDAO.update(course);

                final List<Mentor> mentors = mentorDAO.getAssociatedMentors(CourseID);

                MentorAdapter mentorAdapter = new MentorAdapter(mentors, CourseViewActivity.this); // pass 'this' as the listener
                recyclerViewM.setAdapter(mentorAdapter);
            }
        });

        Button SetAlert = findViewById(R.id.courseDetailsSetAlert);
        SetAlert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();

                if (!areNotificationsEnabled) {
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                }

                if (selectedAssessment != null) {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    // The time at which you want to show notification (in milliseconds)
                    LocalDate dueDate = selectedAssessment.getDueDate();
                    long triggerTime;

                    if (dueDate.equals(LocalDate.now())) {
                        // If the due date is today, set the triggerTime to 5 seconds from now
                        triggerTime = System.currentTimeMillis() + 5000;
                        Log.d("Time", String.valueOf(triggerTime));
                    } else {
                        // If the due date is in the future, set the triggerTime to the start of the due date
                        ZonedDateTime zonedDateTime = dueDate.atStartOfDay(ZoneId.systemDefault());
                        triggerTime = zonedDateTime.toInstant().toEpochMilli();
                    }

                    // Create an Intent to broadcast to MyAlarmReceiver
                    Intent intent = new Intent(CourseViewActivity.this, MyAlarmReceiver.class);

                    // Create a PendingIntent to be triggered when the alarm goes off
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(CourseViewActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    // Schedule the alarm
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                    }

                    Toast.makeText(CourseViewActivity.this, "Alert Set", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CourseViewActivity.this, "Please select an assessment to enable alerts", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button ShareNotes = findViewById(R.id.ShareNotes);
        ShareNotes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create the intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                // Set the type of data you need to share
                shareIntent.setType("text/plain");

                // Add the notes to the intent
                shareIntent.putExtra(Intent.EXTRA_TEXT, Note);

                // Start the activity
                startActivity(Intent.createChooser(shareIntent, "Share notes using"));
            }
        });


        Button DeleteMentor = findViewById(R.id.courseDetailsDeleteAssessment);
        DeleteMentor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                assessmentDAO.delete(selectedAssessment);

                final List<Assessment> assessments = assessmentDAO.getAssociatedAssessments(CourseID);

                AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, CourseViewActivity.this); // pass 'this' as the listener
                recyclerView.setAdapter(assessmentAdapter);
            }
        });
    }
}
