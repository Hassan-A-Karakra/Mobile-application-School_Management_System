package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    private TextView studentGradeText;
    private TextView absencesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_grades);

        // Initialize views
        gradesRecyclerView = findViewById(R.id.gradesRecyclerView);
        studentNameText = findViewById(R.id.studentNameText);
        studentClassText = findViewById(R.id.studentClassText);
        averageGradeText = findViewById(R.id.averageGradeText);
        statusText = findViewById(R.id.statusText);
        studentGradeText = findViewById(R.id.studentGradeText);
        absencesText = findViewById(R.id.absencesText);

        gradesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get student ID
        int studentId = getIntent().getIntExtra("student_id", -1);
        if (studentId == -1) {
            SharedPreferences prefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
            studentId = prefs.getInt("student_id", -1);
        }

        if (studentId == -1) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadStudentData(studentId);
    }

    private void loadStudentData(int studentId) {
        String url = BASE_URL + "student_get_grades.php";
        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", studentId);
            Log.d(TAG, "Sending student_id from Android: " + studentId);
        } catch (Exception e) {
            Log.e(TAG, "Error preparing request JSON: " + e.getMessage());
            Toast.makeText(this, "Error preparing request", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        String status = response.getString("status");

                        if (status.equals("success")) {
                            // Get student info
                            JSONObject studentInfo = response.getJSONObject("student_info");
                            studentNameText.setText(studentInfo.getString("name"));
                            studentClassText.setText("Grade: " + studentInfo.getString("grade"));

                            // Get grades
                            JSONArray gradesArray = response.getJSONArray("grades");
                            List<StudentGradeItem> studentGradeItems = new ArrayList<>();

                            // Get attendance
                            JSONArray attendanceArray = response.getJSONArray("attendance");
                            Map<String, Integer> attendanceMap = new HashMap<>();
                            int totalAbsences = 0; // Initialize total absences here
                            for (int i = 0; i < attendanceArray.length(); i++) {
                                JSONObject attendance = attendanceArray.getJSONObject(i);
                                String subject = attendance.getString("subject");
                                int absenceCount = attendance.getInt("absence_count");
                                attendanceMap.put(subject, absenceCount);
                                totalAbsences += absenceCount; // Sum all absences
                            }

                            // Process grades (no longer calculating totalAbsences here)
                            for (int i = 0; i < gradesArray.length(); i++) {
                                JSONObject grade = gradesArray.getJSONObject(i);
                                String subject = grade.getString("subject_name");
                                String gradeValue = grade.getString("grade");
                                String published = grade.optString("published", "0");
                                String teacherName = grade.optString("teacher_name", "N/A");

                                if ("1".equals(published)) {
                                    // int absences = attendanceMap.getOrDefault(subject, 0); // Commented out to use totalAbsences
                                    studentGradeItems.add(new StudentGradeItem(subject, gradeValue, String.valueOf(totalAbsences), teacherName));
                                }
                            }

                            // Update UI with grades and absences
                            if (!studentGradeItems.isEmpty()) {
                                double average = response.getDouble("average_grade");
                                updateGradeStatus(average);
                                absencesText.setText("Total Absences: " + totalAbsences); // Display the calculated total absences

                                Collections.sort(studentGradeItems, (a, b) ->
                                        a.getSubject().compareToIgnoreCase(b.getSubject()));

                                gradesRecyclerView.setAdapter(new StudentGradesAdapter(studentGradeItems));
                            } else {
                                averageGradeText.setText("No grades available");
                                statusText.setText("Status: Not Available");
                                statusText.setTextColor(getResources().getColor(R.color.needs_improvement));
                                absencesText.setText("Total Absences: " + totalAbsences); // Display total absences even if no grades
                                studentGradeText.setText("Grade: N/A");
                            }
                        } else {
                            // If status is "error", display the error message from the PHP script
                            String errorMessage = response.getString("message");
                            Toast.makeText(StudentGradesActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            // You might also want to clear existing data or show an empty state
                            studentNameText.setText("Error");
                            studentClassText.setText("");
                            averageGradeText.setText("");
                            statusText.setText("Status: Error");
                            statusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            absencesText.setText("");
                            studentGradeText.setText("");
                            gradesRecyclerView.setAdapter(null); // Clear the RecyclerView
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response: " + e.getMessage(), e);
                        Toast.makeText(StudentGradesActivity.this, "Error processing data", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Network error: " + error.getMessage(), error);
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
            // loadStudentData(studentId);

        }
    }
}