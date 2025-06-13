package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Added for logging
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class StudentGradesActivity extends AppCompatActivity {

    private static final String TAG = "GradesActivity";
    private RecyclerView gradesRecyclerView;
    private static final String BASE_URL = "http://10.0.2.2/student_system/";
    private TextView studentNameText;
    private TextView studentClassText;
    private TextView averageGradeText;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_grades);

        gradesRecyclerView = findViewById(R.id.gradesRecyclerView);
        studentNameText = findViewById(R.id.studentNameText);
        studentClassText = findViewById(R.id.studentClassText);
        averageGradeText = findViewById(R.id.averageGradeText);
        statusText = findViewById(R.id.statusText);

        gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // First try to get student ID from intent extras
        int studentId = getIntent().getIntExtra("student_id", -1);
        Log.d(TAG, "Student ID from Intent: " + studentId); // Log ID from Intent

        // If not found in intent, try SharedPreferences
        if (studentId == -1) {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            studentId = prefs.getInt("student_id", -1);
            Log.d(TAG, "Student ID from SharedPreferences: " + studentId); // Log ID from SharedPreferences
        }

        // If still not found, show error and finish
        if (studentId == -1) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_LONG).show();
            finish();
            Log.e(TAG, "Student ID is still -1. Finishing activity."); // Log the final state
            return;
        }

        // Get student info from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String studentName = prefs.getString("student_name", "Student");
        String studentGrade = prefs.getString("student_grade", "");
        String studentClass = prefs.getString("student_class", "");

        studentNameText.setText(studentName);
        if (!studentClass.isEmpty()) {
            studentClassText.setText("Class: " + studentClass);
        } else if (!studentGrade.isEmpty()) {
            studentClassText.setText("Grade: " + studentGrade);
        } else {
            studentClassText.setVisibility(View.GONE);
        }

        loadGrades(studentId);
    }

    private void loadGrades(int studentId) {
        String url = BASE_URL + "student_get_grades.php";
        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", studentId);
        } catch (Exception e) {
            Log.e(TAG, "Error preparing request JSON: " + e.getMessage(), e);
            Toast.makeText(this, "Error preparing request", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if ("success".equals(response.optString("status"))) {
                            JSONArray gradesArray = response.getJSONArray("grades");
                            List<StudentGradeItem> studentGradeItems = new ArrayList<>();
                            double totalGrade = 0;
                            int gradeCount = 0;

                            for (int i = 0; i < gradesArray.length(); i++) {
                                JSONObject row = gradesArray.getJSONObject(i);
                                String subject = row.getString("subject_name");
                                String grade = row.getString("grade");
                                String published = row.optString("published", "0");

                                if ("1".equals(published)) {
                                    studentGradeItems.add(new StudentGradeItem(subject, grade, ""));
                                    try {
                                        totalGrade += Double.parseDouble(grade);
                                        gradeCount++;
                                    } catch (NumberFormatException e) {
                                        Log.w(TAG, "Invalid grade format for subject " + subject + ": " + grade);
                                    }
                                }
                            }

                            if (gradeCount > 0) {
                                double average = totalGrade / gradeCount;
                                updateGradeStatus(average);
                            } else {
                                averageGradeText.setText("No grades available");
                                statusText.setText("Status: Not Available");
                                statusText.setTextColor(getResources().getColor(R.color.needs_improvement));
                            }

                            Collections.sort(studentGradeItems, (a, b) ->
                                    a.getSubject().compareToIgnoreCase(b.getSubject()));

                            gradesRecyclerView.setAdapter(new StudentGradesAdapter(studentGradeItems));
                        } else {
                            String errorMessage = response.optString("message", "Failed to load grades");
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing grades JSON response: " + e.getMessage(), e);
                        Toast.makeText(this, "Error processing grades data", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Network error during grade fetch: " + error.getMessage(), error);
                    Toast.makeText(this, "Network error: Could not connect to server", Toast.LENGTH_LONG).show();
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void updateGradeStatus(double average) {
        averageGradeText.setText(String.format("Average: %.1f%%", average));

        String status;
        int statusColor;
        if (average >= 90) {
            status = "Excellent";
            statusColor = getResources().getColor(R.color.excellent);
        } else if (average >= 80) {
            status = "Good Standing";
            statusColor = getResources().getColor(R.color.good);
        } else if (average >= 70) {
            status = "Satisfactory";
            statusColor = getResources().getColor(R.color.satisfactory);
        } else {
            status = "Needs Improvement";
            statusColor = getResources().getColor(R.color.needs_improvement);
        }

        statusText.setText("Status: " + status);
        statusText.setTextColor(statusColor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int studentId = prefs.getInt("student_id", -1);
        if (studentId != -1) {
            loadGrades(studentId);
        }
    }
}