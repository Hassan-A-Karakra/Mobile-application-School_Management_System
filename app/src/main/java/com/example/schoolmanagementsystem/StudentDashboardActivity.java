package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class StudentDashboardActivity extends AppCompatActivity {
    private int studentId;
    private String studentName;
    private String studentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        // Initialize NetworkUtils
        NetworkUtils.init(this);

        // Get student data from intent
        studentId = getIntent().getIntExtra("student_id", -1);
        studentName = getIntent().getStringExtra("student_name");
        studentEmail = getIntent().getStringExtra("student_email");

        if (studentId == -1) {
            Toast.makeText(this, "Error: Student data not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set welcome message
        TextView welcomeText = findViewById(R.id.textWelcome);
        welcomeText.setText("Welcome, " + studentName);

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

        // View assignments
        btnAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, AssignmentViewActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Submit assignments
        btnSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, SubmitAssignmentActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Message teacher
        btnCommunicate.setOnClickListener(v ->
                startActivity(new Intent(StudentDashboardActivity.this, CommunicateActivity.class)));

        // Logout
        btnLogout.setOnClickListener(v -> {
            // Clear any stored data if needed
            finish();
        });
    }
}
