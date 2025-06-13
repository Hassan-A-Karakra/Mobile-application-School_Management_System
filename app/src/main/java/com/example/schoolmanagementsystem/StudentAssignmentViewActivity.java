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

public class StudentAssignmentViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAssignmentAdapter adapter;
    private List<Assignment> assignmentList;
    private static final String ASSIGNMENT_URL = "http://10.0.2.2/student_system/student_get_assignments.php";
    private int studentId; // Declare studentId here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignment_view);

        recyclerView = findViewById(R.id.recyclerViewAssignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("student_id", -1); // Retrieve studentId here

        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show();
            finish(); // Consider finishing if essential ID is missing
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

        recyclerView.setAdapter(adapter);

        fetchAssignments(studentId);
    }

    private void fetchAssignments(int studentId) {
        try {
            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("student_id", studentId);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ASSIGNMENT_URL, jsonRequest,
                    response -> {
                        try {
                            Log.d("AssignmentsResponse", response.toString());

                            if (!response.has("assignments") || !response.getBoolean("success")) {
                                Toast.makeText(this, "Failed to load assignments", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            JSONArray assignments = response.getJSONArray("assignments");
                            assignmentList.clear();

                            for (int i = 0; i < assignments.length(); i++) {
                                JSONObject jsonObject = assignments.getJSONObject(i);
                                Assignment assignment = new Assignment();

                                assignment.setId(jsonObject.getInt("id"));
                                assignment.setTitle(jsonObject.getString("title"));
                                assignment.setDescription(jsonObject.getString("description"));
                                assignment.setDueDate(jsonObject.getString("due_date"));

                                if (jsonObject.has("grade") && !jsonObject.isNull("grade")) {
                                    assignment.setGrade(jsonObject.getString("grade"));
                                } else {
                                    assignment.setGrade("Not graded yet");
                                }

                                assignmentList.add(assignment);
                            }

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("StudentAssignmentView", "Error parsing assignments JSON: " + e.getMessage(), e);
                            Toast.makeText(this, "Error processing assignments data", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Log.e("StudentAssignmentView", "Volley error: " + error.getMessage(), error);
                        Toast.makeText(this, "Network error: Could not load assignments", Toast.LENGTH_SHORT).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            Log.e("StudentAssignmentView", "Error in fetchAssignments: " + e.getMessage(), e);
            Toast.makeText(this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
        }
    }
}