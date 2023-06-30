package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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

// This class is responsible for displaying the list of courses
public class CourseListActivity extends Activity implements CourseAdapter.OnCourseSelectedListener {

    private Course selectedCourse;

    // This method is called when a course is selected from the list
    @Override
    public void onCourseSelected(Course selectedCourse) {
        this.selectedCourse = selectedCourse;
    }

    // This method is called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_list);

        // Setting up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.courseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Getting the database instance and the DAOs
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        CourseDAO courseDAO = db.courseDAO();

        // Fetching all the courses from the database
        final List<Course> courses = courseDAO.getAllCourses();

        // Setting up the adapter for the RecyclerView
        CourseAdapter courseAdapter = new CourseAdapter(courses, this);
        recyclerView.setAdapter(courseAdapter);

        // Setting up the back button
        Button backButton = findViewById(R.id.courseBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the add course button
        Button AddButton = findViewById(R.id.addCourse);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CourseListActivity.this, CourseAddActivity.class);
                startActivity(intent);
            }
        });

        // Setting up the view course button
        Button ViewButton = findViewById(R.id.viewCourse);
        ViewButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
              Intent intent = new Intent(CourseListActivity.this, CourseViewActivity.class);

              if (selectedCourse != null) {
                  Integer courseID = selectedCourse.getCourseID();
                  String title = selectedCourse.getCourseName() != null ? selectedCourse.getCourseName().toString() : null;
                  String startDate = selectedCourse.getStartDate() != null ? selectedCourse.getStartDate().toString() : null;
                  String endDate = selectedCourse.getEndDate() != null ? selectedCourse.getEndDate().toString() : null;
                  CourseStatus Status = selectedCourse.getCourseStatus();
                  NoteDAO noteDAO = db.noteDAO();
                  Note NoteText = noteDAO.getNoteText(courseID);

                  // Passing the course details to the next activity
                  intent.putExtra("CourseID", courseID);
                  intent.putExtra("Title", title);
                  intent.putExtra("StartDate", startDate);
                  intent.putExtra("EndDate", endDate);
                  intent.putExtra("Status", Status.toString());
                  intent.putExtra("Note", NoteText.getNote());

                  startActivity(intent);
              } else {
                  Toast.makeText(CourseListActivity.this, "Please select a course to edit", Toast.LENGTH_SHORT).show();
              }
          }
      });

        // Setting up the edit course button
        Button EditButton = findViewById(R.id.editCourse);
        EditButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                        intent.putExtra("Note", note.getNote());
                    }

                    // Passing the course details to the next activity
                    intent.putExtra("CourseID", courseID);
                    intent.putExtra("Title", title);
                    intent.putExtra("StartDate", startDate);
                    intent.putExtra("EndDate", endDate);
                    intent.putExtra("Status", Status.toString());

                    startActivity(intent);
                } else {
                    Toast.makeText(CourseListActivity.this, "Please select a course to edit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setting up the delete course button
        Button DeleteButton = findViewById(R.id.deleteCourse);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Deleting the selected course from the database
                courseDAO.delete(selectedCourse);

                // Refreshing the list of courses
                final List<Course> courses = courseDAO.getAllCourses();

                CourseAdapter courseAdapter = new CourseAdapter(courses, CourseListActivity.this);
                recyclerView.setAdapter(courseAdapter);
            }
        });

        // Starting a new thread to fetch the courses from the database
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Course> courses = courseDAO.getAllCourses();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CourseAdapter courseAdapter = new CourseAdapter(courses, CourseListActivity.this);
                        recyclerView.setAdapter(courseAdapter);

                        // Setting up the SearchView
                        SearchView searchView = findViewById(R.id.searchCourses);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                courseAdapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                    }
                });
            }
        }).start();

    }
}
