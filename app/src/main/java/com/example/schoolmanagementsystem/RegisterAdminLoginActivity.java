package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterAdminLoginActivity extends AppCompatActivity {

    EditText usernameField, passwordField;
    Button loginButton;
    CheckBox rememberMeCheckbox;
    final String correctUsername = "admin";
    final String correctPassword = "admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin_login);

        usernameField = findViewById(R.id.adminUsername);
        passwordField = findViewById(R.id.adminPassword);
        loginButton = findViewById(R.id.adminLoginButton);
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        SharedPreferences preferences = getSharedPreferences("AdminLoginPrefs", MODE_PRIVATE);
        String savedUsername = preferences.getString("username", "");
        String savedPassword = preferences.getString("password", "");
        boolean rememberMe = preferences.getBoolean("rememberMe", false);

        if (!savedUsername.isEmpty() && !savedPassword.isEmpty() && rememberMe) {
            usernameField.setText(savedUsername);
            passwordField.setText(savedPassword);
            rememberMeCheckbox.setChecked(true);
        }

        loginButton.setOnClickListener(v -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (username.equals(correctUsername) && password.equals(correctPassword)) {
                if (rememberMeCheckbox.isChecked()) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putBoolean("rememberMe", true);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                }

                Intent intent = new Intent(RegisterAdminLoginActivity.this, RegisterAdminDashboardActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
