package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.CourseAdapter;
import com.example.wgumobilelegit.Objects.Course;
import com.example.wgumobilelegit.Objects.CourseStatus;
import com.example.wgumobilelegit.Objects.Note;
import com.example.wgumobilelegit.dao.CourseDAO;
import com.example.wgumobilelegit.dao.NoteDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.util.List;

public class CourseListActivity extends Activity implements CourseAdapter.OnCourseSelectedListener {

    private Course selectedCourse;

    @Override
    public void onCourseSelected(Course selectedCourse) {
        // This method will be called when an item is selected
        this.selectedCourse = selectedCourse;

        Log.d("TroubleShoot", selectedCourse.getCourseStatus().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);

        RecyclerView recyclerView = findViewById(R.id.courseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        CourseDAO courseDAO = db.courseDAO();

        final List<Course> courses = courseDAO.getAllCourses();

        CourseAdapter courseAdapter = new CourseAdapter(courses, this); // pass 'this' as the listener
        recyclerView.setAdapter(courseAdapter);

        Button backButton = findViewById(R.id.courseBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button AddButton = findViewById(R.id.addCourse);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseListActivity.this, CourseAddActivity.class);
                startActivity(intent);
            }
        });

        Button ViewButton = findViewById(R.id.viewCourse);
        ViewButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              // Code here executes on main thread after user presses button
              Intent intent = new Intent(CourseListActivity.this, CourseViewActivity.class);

              if (selectedCourse != null) {
                  Integer courseID = selectedCourse.getCourseID();
                  String title = selectedCourse.getCourseName() != null ? selectedCourse.getCourseName().toString() : null;
                  String startDate = selectedCourse.getStartDate() != null ? selectedCourse.getStartDate().toString() : null;
                  String endDate = selectedCourse.getEndDate() != null ? selectedCourse.getEndDate().toString() : null;
                  CourseStatus Status = selectedCourse.getCourseStatus();
                  NoteDAO noteDAO = db.noteDAO();
                  Note NoteText = noteDAO.getNoteText(courseID);


                  intent.putExtra("CourseID", courseID);
                  intent.putExtra("Title", title);
                  intent.putExtra("StartDate", startDate);
                  intent.putExtra("EndDate", endDate);
                  intent.putExtra("Status", Status.toString());
                  intent.putExtra("Note", NoteText.getNote());

                  startActivity(intent);
              } else {
                  // Handle the case where no course is selected
                  // For example, show a Toast message
                  Toast.makeText(CourseListActivity.this, "Please select a course to edit", Toast.LENGTH_SHORT).show();
              }
          }
      });


        Button EditButton = findViewById(R.id.editCourse);
        EditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(CourseListActivity.this, CourseEditActivity.class);

                if (selectedCourse != null) {
                    Integer courseID = selectedCourse.getCourseID();
                    String title = selectedCourse.getCourseName() != null ? selectedCourse.getCourseName().toString() : null;
                    String startDate = selectedCourse.getStartDate() != null ? selectedCourse.getStartDate().toString() : null;
                    String endDate = selectedCourse.getEndDate() != null ? selectedCourse.getEndDate().toString() : null;
                    CourseStatus Status = selectedCourse.getCourseStatus();

                    NoteDAO noteDao = db.noteDAO();
                    Note note = noteDao.getNoteText(courseID);

                    if (note != null) {
                        Log.d("Troubleshooting", "Status "+String.valueOf(Status));
                        Log.d("Troubleshooting", "CourseID "+String.valueOf(courseID));
                        Log.d("Troubleshooting", "Note "+String.valueOf(note.getNote()));

                        intent.putExtra("Note", note.getNote());
                    } else {
                        Log.d("Troubleshooting", "Note not found for courseID: " + String.valueOf(courseID));
                    }

                    intent.putExtra("CourseID", courseID);
                    intent.putExtra("Title", title);
                    intent.putExtra("StartDate", startDate);
                    intent.putExtra("EndDate", endDate);
                    intent.putExtra("Status", Status.toString());

                    startActivity(intent);
                } else {
                    // Handle the case where no course is selected
                    // For example, show a Toast message
                    Toast.makeText(CourseListActivity.this, "Please select a course to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button DeleteButton = findViewById(R.id.deleteCourse);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

                courseDAO.delete(selectedCourse);

                final List<Course> courses = courseDAO.getAllCourses();

                CourseAdapter courseAdapter = new CourseAdapter(courses, CourseListActivity.this); // pass 'this' as the listener
                recyclerView.setAdapter(courseAdapter);
            }
        });


        // Start a new thread for database operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Retrieve all the courses from the Room database
                final List<Course> courses = courseDAO.getAllCourses();

                // Run the adapter setting on the UI thread since it affects the UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CourseAdapter courseAdapter = new CourseAdapter(courses, CourseListActivity.this);
                        recyclerView.setAdapter(courseAdapter);
                    }
                });
            }
        }).start();

    }
}
