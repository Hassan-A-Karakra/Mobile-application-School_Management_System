package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StudentDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        Button btnSchedule = findViewById(R.id.btnSchedule);
        Button btnGrades = findViewById(R.id.btnGrades);
        Button btnAssignments = findViewById(R.id.btnAssignments);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnCommunicate = findViewById(R.id.btnCommunicate);
        Button btnLogout = findViewById(R.id.btnLogout);

        // View class schedule
        btnSchedule.setOnClickListener(v ->
                startActivity(new Intent(StudentDashboardActivity.this, ClassScheduleActivity.class)));

        // View grades
        btnGrades.setOnClickListener(v ->
                startActivity(new Intent(StudentDashboardActivity.this, ReportsActivity.class)));

        // ✅ View assignments (replaced wrong GradeInputActivity)
        btnAssignments.setOnClickListener(v ->
                startActivity(new Intent(StudentDashboardActivity.this, AssignmentViewActivity.class)));

        // Submit assignments
        btnSubmit.setOnClickListener(v ->
                startActivity(new Intent(StudentDashboardActivity.this, SubmitAssignmentActivity.class)));

        // Message teacher
        btnCommunicate.setOnClickListener(v ->
                startActivity(new Intent(StudentDashboardActivity.this, CommunicateActivity.class)));

        // Logout
        btnLogout.setOnClickListener(v -> finish());
    }
}
