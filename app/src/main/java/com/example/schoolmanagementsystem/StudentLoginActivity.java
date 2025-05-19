package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class StudentLoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        EditText username = findViewById(R.id.editTextStudentUsername);
        EditText password = findViewById(R.id.editTextStudentPassword);
        Button loginBtn = findViewById(R.id.buttonStudentLogin);

        loginBtn.setOnClickListener(v -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (user.equals("student") && pass.equals("1234")) {
                startActivity(new Intent(StudentLoginActivity.this, StudentDashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}