package com.example.wgumobilelegit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Adapters.TermAdapter;
import com.example.wgumobilelegit.Objects.Term;
import com.example.wgumobilelegit.dao.TermDAO;
import com.example.wgumobilelegit.database.AppDatabase;

import java.util.List;

public class TermListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_list);

        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button AddButton = findViewById(R.id.AddTerm);
        AddButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(TermListActivity.this, TermAddActivity.class);
                startActivity(intent);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.TermList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        TermDAO termDAO = db.termDAO();

        // Start a new thread for database operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Retrieve all the terms from the Room database
                final List<Term> terms = termDAO.getAllTerms();

                // Run the adapter setting on the UI thread since it affects the UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TermAdapter termAdapter = new TermAdapter(terms);
                        recyclerView.setAdapter(termAdapter);
                    }
                });
            }
        }).start();

    }
}
