package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.time.LocalDate;


public class TermAddActivity extends Activity {

    public LocalDate StartDate;
    public LocalDate EndDate;
    public String TermTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details_add);

        Button backButton = findViewById(R.id.AddTermBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermAddActivity.this, TermListActivity.class);
                startActivity(intent);
            }
        });


        Button StartDate = findViewById(R.id.GoTermDetailsStart);
        StartDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermAddActivity.this, TermStartDateActivity.class);
                startActivity(intent);
            }
        });
    }
}
