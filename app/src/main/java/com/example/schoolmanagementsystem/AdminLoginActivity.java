package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginButton;
    final String correctUsername = "admin";
    final String correctPassword = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        usernameField = findViewById(R.id.adminUsername);
        passwordField = findViewById(R.id.adminPassword);
        loginButton = findViewById(R.id.adminLoginButton);

        loginButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
