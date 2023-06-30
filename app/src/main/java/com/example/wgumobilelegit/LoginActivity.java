package com.example.wgumobilelegit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.ComponentActivity;

import com.example.wgumobilelegit.Objects.User;
import com.example.wgumobilelegit.dao.MentorDAO;
import com.example.wgumobilelegit.dao.UserDAO;
import com.example.wgumobilelegit.database.AppDatabase;

public class LoginActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        UserDAO userDAO = db.userDAO();

        User user = new User("Student", "password");
        userDAO.insert(user);

        Button TermButton = findViewById(R.id.loginSubmit);
        TermButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                EditText Username = findViewById(R.id.editUser);
                String UserText = String.valueOf(Username.getText());

                EditText Password = findViewById(R.id.editPassword);
                String PasswordText = String.valueOf(Password.getText());

                String DBUser = String.valueOf(userDAO.getUserName(1).getUserName());
                String DBPassword = String.valueOf(userDAO.getUserPassword(1).getUserPassword());

                Log.d("Troubleshoot", "Password Field: "+PasswordText);
                Log.d("Troubleshoot", "Password DB: "+DBPassword);
                Log.d("Troubleshoot", "User Field: "+DBUser);
                Log.d("Troubleshoot", "User DB: "+UserText);

                if(UserText.equals(DBUser) && PasswordText.equals(DBPassword)){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
