package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {

    private int studentId;
    private String studentName;
    private String studentEmail;
    private String studentGrade;
    private int studentAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Get student data from login
        studentId = getIntent().getIntExtra("student_id", -1);
        studentName = getIntent().getStringExtra("student_name");
        studentEmail = getIntent().getStringExtra("student_email");
        studentGrade = getIntent().getStringExtra("student_grade");
        studentAge = getIntent().getIntExtra("student_age", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Missing student data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set welcome message
        TextView welcomeText = findViewById(R.id.textWelcome);
        welcomeText.setText("Welcome, " + studentName);

        // Buttons
        Button btnSchedule = findViewById(R.id.btnSchedule);
        Button btnGrades = findViewById(R.id.btnGrades);
        Button btnAssignments = findViewById(R.id.btnAssignments);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnCommunicate = findViewById(R.id.btnCommunicate);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Schedule
        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClassScheduleActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Grades
        btnGrades.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportsActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Assignments
        btnAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(this, AssignmentsActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Submit Assignments
        btnSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(this, SubmitAssignmentActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Communicate
        btnCommunicate.setOnClickListener(v -> {
            Intent intent = new Intent(this, CommunicateActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
