package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentAssignmentsActivity extends AppCompatActivity {

    private static final String TAG = "StudentAssignmentsAct";
    private RecyclerView assignmentsRecyclerView;
    private StudentAssignmentAdapter adapter;
    private List<Assignment> assignmentList;
    private static final String ASSIGNMENT_URL = "http://10.0.2.2/student_system/student_get_assignments.php";
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignments);

        assignmentsRecyclerView = findViewById(R.id.assignmentsRecyclerView);
        assignmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("student_id", -1); // Retrieve studentId here

        if (studentId == -1) {
            Toast.makeText(this, "Student not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        assignmentList = new ArrayList<>();
        adapter = new StudentAssignmentAdapter(this, assignmentList, assignment -> {
            Intent intent = new Intent(this, StudentSubmitAssignmentActivity.class);
            intent.putExtra("assignment_id", assignment.getId());
            intent.putExtra("student_id", studentId); // FIX: Pass studentId here
            intent.putExtra("title", assignment.getTitle());
            intent.putExtra("description", assignment.getDescription());
            intent.putExtra("due_date", assignment.getDueDate());
            intent.putExtra("grade", assignment.getGrade());
            startActivity(intent);
        });
        assignmentsRecyclerView.setAdapter(adapter);

        loadAssignments();
    }

    private void loadAssignments() {
        Log.d(TAG, "Attempting to load assignments for student ID: " + studentId);
        try {
            JSONObject postData = new JSONObject();
            postData.put("student_id", studentId);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ASSIGNMENT_URL, postData,
                    response -> {
                        try {
                            Log.d(TAG, "Server Response: " + response.toString());

                            if (response.has("assignments") && "success".equals(response.optString("status"))) {
                                JSONArray assignmentsJsonArray = response.getJSONArray("assignments");
                                assignmentList.clear();

                                for (int i = 0; i < assignmentsJsonArray.length(); i++) {
                                    JSONObject obj = assignmentsJsonArray.getJSONObject(i);
                                    Assignment assignment = new Assignment(
                                            obj.getInt("id"),
                                            obj.getString("title"),
                                            obj.getString("description"),
                                            obj.getString("due_date"),
                                            obj.optString("assignment_class", ""),
                                            obj.optString("assignment_subject", ""),
                                            obj.optString("grade", "Not graded yet")
                                    );
                                    assignmentList.add(assignment);
                                }

                                adapter.notifyDataSetChanged();

                                if (assignmentList.isEmpty()) {
                                    Toast.makeText(this, "No assignments found.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "No assignments found for student ID: " + studentId);
                                } else {
                                    Toast.makeText(this, "Assignments loaded successfully.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                String errorMessage = response.optString("message", "Unknown error loading assignments.");
                                Toast.makeText(this, "Failed to load assignments: " + errorMessage, Toast.LENGTH_LONG).show();
                                Log.e(TAG, "Failed to load assignments. Server response: " + response.toString());
                            }
                        } catch (JSONException e) {
                            Toast.makeText(this, "Error parsing assignments data.", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
                        }
                    },
                    error -> {
                        String errorMessage = "Network error";
                        if (error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        Toast.makeText(this, "Failed to load assignments: " + errorMessage, Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Volley error: " + errorMessage, error);

                        if (error.networkResponse != null) {
                            Log.e(TAG, "Volley network response code: " + error.networkResponse.statusCode);
                            Log.e(TAG, "Volley network response data: " + new String(error.networkResponse.data));
                        }
                    }
            );

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            Log.e(TAG, "Error in loadAssignments: " + e.getMessage(), e);
            Toast.makeText(this, "An unexpected error occurred while loading assignments.", Toast.LENGTH_LONG).show();
        }
    }
}