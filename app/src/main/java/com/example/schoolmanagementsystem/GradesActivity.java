package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class GradesActivity extends AppCompatActivity {

    private static final String TAG = "GradesActivity";
    private RecyclerView gradesRecyclerView;
    private static final String BASE_URL = "http://10.0.2.2/student_system/";
    private TextView studentNameText;
    private TextView studentIdText;
    private TextView averageGradeText;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        try {
            // Initialize views
            gradesRecyclerView = findViewById(R.id.gradesRecyclerView);
            studentNameText = findViewById(R.id.studentNameText);
            studentIdText = findViewById(R.id.studentIdText);
            averageGradeText = findViewById(R.id.averageGradeText);
            statusText = findViewById(R.id.statusText);

            if (gradesRecyclerView == null || studentNameText == null ||
                    studentIdText == null || averageGradeText == null || statusText == null) {
                throw new IllegalStateException("Required views not found in layout");
            }

            gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Get student information from SharedPreferences
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            int studentId = prefs.getInt("student_id", -1);
            String studentName = prefs.getString("student_name", "");
            String studentClass = prefs.getString("student_class", "");

            if (studentId == -1) {
                Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Set student information
            studentNameText.setText(studentName);
            studentIdText.setText("ID: " + studentId + " | Class: " + studentClass);

            loadGrades(studentId);
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
            Toast.makeText(this, "Error initializing grades view: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadGrades(int studentId) {
        String url = BASE_URL + "get_grades.php";
        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", studentId);
        } catch (Exception e) {
            Toast.makeText(this, "Error preparing request", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if ("success".equals(response.optString("status"))) {
                            JSONArray gradesArray = response.getJSONArray("grades");
                            List<GradeItem> gradeItems = new ArrayList<>();
                            Set<String> seenSubjects = new HashSet<>();
                            double totalGrade = 0;
                            int gradeCount = 0;

                            for (int i = 0; i < gradesArray.length(); i++) {
                                JSONObject row = gradesArray.getJSONObject(i);
                                String subject = row.getString("subject");
                                String grade = row.getString("grade");
                                String teacher = row.optString("teacher", "N/A"); // Extract teacher name here
                                if (!seenSubjects.contains(subject)) {
                                    gradeItems.add(new GradeItem(subject, grade, teacher)); // Pass teacher name
                                    seenSubjects.add(subject);

                                    // Calculate average
                                    try {
                                        totalGrade += Double.parseDouble(grade);
                                        gradeCount++;
                                    } catch (NumberFormatException e) {
                                        Log.e(TAG, "Invalid grade format: " + grade);
                                    }
                                }
                            }

                            // Update average and status
                            if (gradeCount > 0) {
                                double average = totalGrade / gradeCount;
                                updateGradeStatus(average);
                            } else {
                                averageGradeText.setText("No grades available");
                                statusText.setText("Status: Not Available");
                            }

                            // Sort grades by subject name
                            Collections.sort(gradeItems, (a, b) ->
                                    a.getSubject().compareToIgnoreCase(b.getSubject()));

                            gradesRecyclerView.setAdapter(new GradesAdapter(gradeItems));
                        } else {
                            String errorMessage = response.optString("message", "Unknown error occurred");
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing grades", e);
                        Toast.makeText(this, "Error reading grades: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Network error", error);
                    Toast.makeText(this, "Network error: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
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
        // Refresh grades when activity resumes
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int studentId = prefs.getInt("student_id", -1);
        if (studentId != -1) {
            loadGrades(studentId);
        }
    }
}