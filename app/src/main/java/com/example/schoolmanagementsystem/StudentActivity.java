package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class StudentActivity extends AppCompatActivity {
    private static final String TAG = "StudentActivity";
    private TextView textWelcome;
    private MaterialButton btnSchedule;
    private MaterialButton btnGrades;
    private MaterialButton btnAssignments;
    private MaterialButton btnSubmit;
    private MaterialButton btnCommunicate;
    private MaterialButton btnLogout;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        // Get student ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("student_id", -1);
        String studentName = prefs.getString("student_name", "Student");

        if (studentId == -1) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        setupWelcomeText(studentName);
        setupButtons();
    }

    private void initializeViews() {
        try {
            textWelcome = findViewById(R.id.textWelcome);
            btnSchedule = findViewById(R.id.btnSchedule);
            btnGrades = findViewById(R.id.btnGrades);
            btnAssignments = findViewById(R.id.btnAssignments);
            btnCommunicate = findViewById(R.id.btnCommunicate);
        } catch (Exception e) {
            Log.e(TAG, "Error initializing views", e);
            Toast.makeText(this, "Error initializing views: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupWelcomeText(String studentName) {
        textWelcome.setText("Welcome, " + studentName);
    }

    private void setupButtons() {
        // Schedule Button
        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(this, studentClassScheduleActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Grades Button
        btnGrades.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this, StudentGradesActivity.class);
                intent.putExtra("student_id", studentId);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error opening grades", e);
                Toast.makeText(this, "Error opening grades: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Assignments Button
        btnAssignments.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentAssignmentsActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Submit Assignment Button
        btnSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentSubmitAssignmentActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Communicate Button
        btnCommunicate.setOnClickListener(v -> {
            Intent intent = new Intent(this, TeacherCommunicateActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        // Logout Button
        btnLogout.setOnClickListener(v -> {
            // Clear SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

            // Return to login screen
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}