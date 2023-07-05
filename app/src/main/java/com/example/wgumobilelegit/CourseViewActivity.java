package com.example.wgumobilelegit;

// Importing necessary libraries and classes
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

// Main class for the CourseViewActivity
public class CourseViewActivity extends Activity implements AssessmentAdapter.OnAssessmentSelectedListener,MentorAdapter.OnMentorSelectedListener {

    // Declaring variables
    public LocalDate StartDateValue;
    public LocalDate EndDateValue;
    public String CourseTitle;
    public Assessment selectedAssessment;
    public Mentor selectedMentor;

    // Method to handle the selection of an assessment
    @Override
    public void onAssessmentSelected(Assessment selectedAssessment) {
        this.selectedAssessment = selectedAssessment;
        Log.d("TroubleShoot", selectedAssessment.getAssessmentName()+" "+selectedAssessment.getAssessmentId());
    }

    // Method to handle the selection of a mentor
    @Override
    public void onMentorSelected(Mentor selectedMentor) {
        this.selectedMentor = selectedMentor;
    }

    // Method to handle the creation of the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_details);

        TextView Notes = findViewById(R.id.courseDetailsNotesValue);

        // Getting database access
        Context context = getApplicationContext();
        AppDatabase db = AppDatabase.getDbInstance(context);

        CourseDAO courseDAO = db.courseDAO();
        AssessmentDAO assessmentDAO = db.AssessmentDAO();
        MentorDAO mentorDAO = db.mentorDAO();

        // Extracting data from the intent
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

        // Setting the text for the TextViews
        TextView title = findViewById(R.id.detailCourseName);
        title.setText(Title);

        TextView startDate = findViewById(R.id.courseDetailsStartValue);
        startDate.setText(String.valueOf(StartDateParam));

        TextView endDate = findViewById(R.id.courseDetailsEndValue);
        endDate.setText(String.valueOf(EndDateParam));

        TextView StatusVal = findViewById(R.id.courseDetailsStatusValue);
        StatusVal.setText(Status.toString());

        // Setting up the RecyclerView for assessments
        RecyclerView recyclerView = findViewById(R.id.courseDetailsAssessments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<Assessment> assessments = assessmentDAO.getAssociatedAssessments(CourseID);
        Log.d("TroubleShoot", "CourseID"+CourseID);
        Log.d("TroubleShoot", "Ass List"+assessments);

        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, this); // pass 'this' as the listener
        recyclerView.setAdapter(assessmentAdapter);

        // Setting up the RecyclerView for mentors
        RecyclerView recyclerViewM = findViewById(R.id.courseDetailsMentors);
        recyclerViewM.setLayoutManager(new LinearLayoutManager(this));
        final List<Mentor> mentors = mentorDAO.getAssociatedMentors(CourseID);
        Log.d("TroubleShoot", "Ass List"+assessments);

        MentorAdapter mentorAdapter = new MentorAdapter(mentors, this); // pass 'this' as the listener
        recyclerViewM.setAdapter(mentorAdapter);

        // Setting up the back button
        Button backButton = findViewById(R.id.detailBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseViewActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the AddAssessment button
        Button AddAssessment = findViewById(R.id.courseDetailsAddAssessment);
        AddAssessment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

        // Setting up the AddMentor button
        Button AddMentor = findViewById(R.id.courseDetailsAddMentors);
        AddMentor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

        // Setting up the DeleteAssessment button
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

        // Setting up the SetAlert button
        Button SetAlert = findViewById(R.id.courseDetailsSetAlert);
        SetAlert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();

                if (selectedAssessment != null) {
                    if (!areNotificationsEnabled) {
                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                        startActivity(intent);
                    }

                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    // Get the start and end dates
                    LocalDate startDate = selectedAssessment.getStartDate();
                    LocalDate endDate = selectedAssessment.getDueDate();

                    // Schedule the start date alert
                    long startTriggerTime = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    Intent startIntent = new Intent(CourseViewActivity.this, MyAlarmReceiver.class);
                    startIntent.putExtra("Type", 2);
                    PendingIntent startPendingIntent = PendingIntent.getBroadcast(CourseViewActivity.this, 2, startIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTriggerTime, startPendingIntent);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTriggerTime, startPendingIntent);
                    }

                    // Schedule the end date alert
                    long endTriggerTime = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    Intent endIntent = new Intent(CourseViewActivity.this, MyAlarmReceiver.class);
                    endIntent.putExtra("Type", 3);
                    PendingIntent endPendingIntent = PendingIntent.getBroadcast(CourseViewActivity.this, 3, endIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTriggerTime, endPendingIntent);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTriggerTime, endPendingIntent);
                    }

                    Toast.makeText(CourseViewActivity.this, "Alerts Set", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CourseViewActivity.this, "Please select an assessment to enable alerts", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setting up the ShareNotes button
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

        // Setting up the DeleteMentor button
        Button DeleteMentor = findViewById(R.id.courseDetailsDeleteAssessment);
        DeleteMentor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                assessmentDAO.delete(selectedAssessment);

                final List<Assessment> assessments = assessmentDAO.getAssociatedAssessments(CourseID);

                AssessmentAdapter assessmentAdapter = new AssessmentAdapter(assessments, CourseViewActivity.this); // pass 'this' as the listener
                recyclerView.setAdapter(assessmentAdapter);
            }
        });


        Button SetCourseStart = findViewById(R.id.setCourseStart);
        SetCourseStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();

                if (!areNotificationsEnabled) {
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                }

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                // Get the start and end dates
                LocalDate startDate = StartDateValue;

                // Schedule the start date alert
                long startTriggerTime = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                Intent startIntent = new Intent(CourseViewActivity.this, MyAlarmReceiver.class);
                startIntent.putExtra("Type", 0);
                PendingIntent startPendingIntent = PendingIntent.getBroadcast(CourseViewActivity.this, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTriggerTime, startPendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTriggerTime, startPendingIntent);
                }

                Toast.makeText(CourseViewActivity.this, "Course Start Date Alerts Set", Toast.LENGTH_SHORT).show();
            }
        });

        Button SetCourseEnd = findViewById(R.id.setCourseEnd);
        SetCourseEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();

                if (!areNotificationsEnabled) {
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                    startActivity(intent);
                }

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                // Get the start and end dates
                LocalDate endDate = EndDateValue;

                // Schedule the end date alert
                long endTriggerTime = endDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                Intent endIntent = new Intent(CourseViewActivity.this, MyAlarmReceiver.class);
                endIntent.putExtra("Type", 1);
                PendingIntent endPendingIntent = PendingIntent.getBroadcast(CourseViewActivity.this, 1, endIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTriggerTime, endPendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTriggerTime, endPendingIntent);
                }

                Toast.makeText(CourseViewActivity.this, "Course End Date Alerts Set", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
