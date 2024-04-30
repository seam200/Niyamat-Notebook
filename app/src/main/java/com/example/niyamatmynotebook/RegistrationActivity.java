package com.example.niyamatmynotebook;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private MyAppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        emailEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        database = MyAppDatabase.getInstance(this);

        // Initialize the register button and set click listener
        Button registerButton = findViewById(R.id.register_btn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            return;
        }
        if (password.isEmpty() || password.length() < 8) {
            passwordEditText.setError("Password must be at least 8 characters long");
            passwordEditText.requestFocus();
            return;
        }

        User newUser = new User(email, password);
        new InsertUserTask().execute(newUser);
    }

    private class InsertUserTask extends AsyncTask<User, Void, Void> {
        protected Void doInBackground(User... users) {
            // Insert the user into the Room Database
            database.userDao().insert(users[0]);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(RegistrationActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
            // You can navigate to another activity or perform any other action here
            TextView loginLink = findViewById(R.id.need_login_link);
            loginLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                }
            });

        }
    }
}
