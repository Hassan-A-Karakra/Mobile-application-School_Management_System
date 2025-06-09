package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View; // Import for View.GONE
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

public class TeacherGradesActivity extends AppCompatActivity {

    private static final String TAG = "GradesActivity";
    private RecyclerView gradesRecyclerView;
    private static final String BASE_URL = "http://10.0.2.2/student_system/";
    private TextView studentNameText;
    private TextView studentClassText; // For displaying the student's class
    private TextView averageGradeText;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_grades);

        try {
            // Initialize views
            gradesRecyclerView = findViewById(R.id.gradesRecyclerView);
            studentNameText = findViewById(R.id.studentNameText);
            studentClassText = findViewById(R.id.studentClassText); // Initialize the studentClassText TextView
            averageGradeText = findViewById(R.id.averageGradeText);
            statusText = findViewById(R.id.statusText);

            // Basic null checks for layout elements
            if (gradesRecyclerView == null || studentNameText == null ||
                    studentClassText == null || averageGradeText == null || statusText == null) {
                throw new IllegalStateException("Required views not found in layout. Please check activity_grades.xml.");
            }

            gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Get student information from SharedPreferences
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            int studentId = prefs.getInt("student_id", -1);
            String studentName = prefs.getString("student_name", "Student"); // Default name
            String studentClass = prefs.getString("student_class", ""); // Get student class

            if (studentId == -1) {
                Toast.makeText(this, "Student ID not found. Please login again.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            // Set student information to the TextViews
            studentNameText.setText(studentName);
            if (!studentClass.isEmpty()) {
                studentClassText.setText("Class: " + studentClass);
            } else {
                studentClassText.setVisibility(View.GONE); // Hide if class info is not available
            }

            loadGrades(studentId);
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate for GradesActivity: " + e.getMessage(), e);
            Toast.makeText(this, "Error initializing grades view: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            finish(); // Close activity on critical error
        }
    }

    private void loadGrades(int studentId) {
        String url = BASE_URL + "get_grades.php";
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
                            Set<String> seenSubjects = new HashSet<>(); // To avoid duplicate subjects if backend sends them

                            double totalGrade = 0;
                            int gradeCount = 0;

                            for (int i = 0; i < gradesArray.length(); i++) {
                                JSONObject row = gradesArray.getJSONObject(i);
                                String subject = row.getString("subject");
                                String grade = row.getString("grade");
                                String teacher = row.optString("teacher", "N/A"); // Default to "N/A" if teacher is missing

                                // Only add unique subjects to avoid display issues
                                if (!seenSubjects.contains(subject)) {
                                    studentGradeItems.add(new StudentGradeItem(subject, grade, teacher));
                                    seenSubjects.add(subject);

                                    // Calculate average, try-catch for grade parsing
                                    try {
                                        totalGrade += Double.parseDouble(grade);
                                        gradeCount++;
                                    } catch (NumberFormatException e) {
                                        Log.w(TAG, "Invalid grade format encountered for subject " + subject + ": " + grade, e);
                                    }
                                }
                            }

                            // Update overall average and status
                            if (gradeCount > 0) {
                                double average = totalGrade / gradeCount;
                                updateGradeStatus(average);
                            } else {
                                averageGradeText.setText("No grades available");
                                statusText.setText("Status: Not Available");
                                statusText.setTextColor(getResources().getColor(R.color.needs_improvement)); // Grey for N/A
                            }

                            // Sort grades by subject name for consistent display
                            Collections.sort(studentGradeItems, (a, b) ->
                                    a.getSubject().compareToIgnoreCase(b.getSubject()));

                            gradesRecyclerView.setAdapter(new TeacherGradesAdapter(studentGradeItems));
                        } else {
                            String errorMessage = response.optString("message", "Failed to load grades. Unknown error occurred.");
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Backend reported error: " + errorMessage);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing grades JSON response: " + e.getMessage(), e);
                        Toast.makeText(this, "Error processing grades data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Network error during grade fetch: " + error.getMessage(), error);
                    Toast.makeText(this, "Network error: Could not connect to server.", Toast.LENGTH_LONG).show();
                });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request);
    }

    private void updateGradeStatus(double average) {
        averageGradeText.setText(String.format("Average: %.1f%%", average));

        String status;
        int statusColor;
        // Determine status based on average grade
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
        } else {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}