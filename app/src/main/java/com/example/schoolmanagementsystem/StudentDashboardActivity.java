package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
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

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("student_id", -1);
        studentName = prefs.getString("student_name", "Student");
        studentEmail = prefs.getString("student_email", "unknown@example.com");
        studentGrade = prefs.getString("student_grade", "N/A");
        studentAge = prefs.getInt("student_age", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Missing student data. Please login.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView welcomeText = findViewById(R.id.textWelcome);
        welcomeText.setText("Welcome, " + studentName);

        Button btnSchedule = findViewById(R.id.btnSchedule);
        Button btnGrades = findViewById(R.id.btnGrades);
        Button btnAssignments = findViewById(R.id.btnAssignments);
        // Button btnSubmit = findViewById(R.id.btnSubmit); // Removed as it's no longer in the layout
        Button btnCommunicate = findViewById(R.id.btnCommunicate);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, ClassScheduleActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        btnGrades.setOnClickListener(v -> {
            Intent intent = new Intent(this, GradesActivity.class);
            intent.putExtra("student_id", studentId);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("student_name", studentName);
            editor.putString("student_class", studentGrade);
            editor.apply();
            startActivity(intent);
        });

        btnAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(this, AssignmentsActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Removed the onClickListener for btnSubmit as the button no longer exists in the layout.
        // The functionality to submit assignments is now handled within the AssignmentsActivity.

        btnCommunicate.setOnClickListener(v -> {
            Intent intent = new Intent(this, CommunicateActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this, StudentLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}