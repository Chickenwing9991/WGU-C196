package com.example.wgumobilelegit;

import androidx.activity.ComponentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        Button TermButton = findViewById(R.id.TermButton);
        TermButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MainActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });

        Button CourseButton = findViewById(R.id.CoursesButton);
        CourseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
                startActivity(intent);
            }
        });

        Button AssessmentButton = findViewById(R.id.AssessmentsButton);
        AssessmentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MainActivity.this, AssessmentListActivity.class);
                startActivity(intent);
            }
        });


        Button MentorButton = findViewById(R.id.MentorButton);
        MentorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(MainActivity.this, MentorListActivity.class);
                startActivity(intent);
            }
        });
    }
}
