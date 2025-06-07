package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AssignmentsActivity extends AppCompatActivity {

    private static final String TAG = "AssignmentsActivity";
    private RecyclerView assignmentsRecyclerView;
    private AssignmentAdapter assignmentAdapter;
    private List<Assignment> assignmentList;
    private int studentId;
    private String studentGrade;

    private static final String BASE_URL = "http://10.0.2.2/student_system/";
    private static final String ASSIGNMENTS_URL = BASE_URL + "get_assignments.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        initializeViews();
        loadStudentData();
        setupRecyclerView();

        if (isNetworkAvailable()) {
            fetchAssignments();
        } else {
            Toast.makeText(this, "No internet connection. Please check your network and try again.", Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        assignmentsRecyclerView = findViewById(R.id.assignmentsRecyclerView);
        assignmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assignmentList = new ArrayList<>();
    }

    private void loadStudentData() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("student_id", -1);
        studentGrade = prefs.getString("student_grade", "");

        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found. Please login again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (studentGrade.isEmpty() || studentGrade.equals("N/A")) {
            Toast.makeText(this, "Student grade not available. Some assignments may not be visible.", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Student grade is missing or N/A. Assignments might not be filtered correctly.");
        }
    }

    private void setupRecyclerView() {
        assignmentAdapter = new AssignmentAdapter(this, assignmentList, assignment -> {
            if (isNetworkAvailable()) {
                Intent intent = new Intent(AssignmentsActivity.this, SubmitAssignmentActivity.class);
                intent.putExtra("assignment_id", assignment.getId());
                intent.putExtra("student_id", studentId);
                intent.putExtra("assignment_title", assignment.getTitle());
                intent.putExtra("due_date", assignment.getDueDate());
                startActivity(intent);
            } else {
                Toast.makeText(this, "No internet connection. Please check your network and try again.", Toast.LENGTH_LONG).show();
            }
        });
        assignmentsRecyclerView.setAdapter(assignmentAdapter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void fetchAssignments() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", studentId);
            if (!studentGrade.isEmpty() && !studentGrade.equals("N/A")) {
                postData.put("student_grade", studentGrade);
                Log.d(TAG, "Fetching assignments for grade: " + studentGrade);
            } else {
                Log.d(TAG, "Fetching all assignments (no grade filter applied)");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error preparing request JSON: " + e.getMessage(), e);
            Toast.makeText(this, "Error preparing assignment request", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ASSIGNMENTS_URL, postData,
                this::handleAssignmentsResponse,
                error -> {
                    Log.e(TAG, "Network error: " + error.getMessage(), error);
                    String errorMessage = "Could not load assignments. Please try again later.";
                    if (error.networkResponse != null) {
                        errorMessage = "Server error: " + error.networkResponse.statusCode;
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void handleAssignmentsResponse(JSONObject response) {
        try {
            if ("success".equals(response.optString("status"))) {
                JSONArray assignmentsArray = response.getJSONArray("assignments");
                assignmentList.clear();

                for (int i = 0; i < assignmentsArray.length(); i++) {
                    JSONObject assignmentJson = assignmentsArray.getJSONObject(i);
                    Assignment assignment = new Assignment(
                            assignmentJson.getInt("id"),
                            assignmentJson.getString("title"),
                            assignmentJson.getString("description"),
                            assignmentJson.getString("due_date")
                    );
                    assignmentList.add(assignment);
                }

                assignmentAdapter.notifyDataSetChanged();

                if (assignmentList.isEmpty()) {
                    Toast.makeText(this, "No assignments available for your grade.", Toast.LENGTH_LONG).show();
                }
            } else {
                String errorMessage = response.optString("message", "Unknown error fetching assignments");
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Backend error: " + errorMessage);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing assignments: " + e.getMessage(), e);
            Toast.makeText(this, "Error processing assignments data", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable()) {
            fetchAssignments();
        }
    }
}