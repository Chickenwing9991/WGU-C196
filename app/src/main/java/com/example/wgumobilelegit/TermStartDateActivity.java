package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.time.LocalDate;

public class TermStartDateActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_details_add_start);

        Button SaveButton = findViewById(R.id.term_details_StartDateOK);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermStartDateActivity.this, TermAddActivity.class);
                startActivity(intent);
            }
        });

    }
}
