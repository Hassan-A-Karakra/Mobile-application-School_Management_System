package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
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

public class StudentAssignmentViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAssignmentAdapter adapter;
    private List<Assignment> assignmentList;
    private static final String ASSIGNMENT_URL = "http://10.0.2.2/student_system/get_assignments.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignment_view);

        recyclerView = findViewById(R.id.recyclerViewAssignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assignmentList = new ArrayList<>();
        adapter = new StudentAssignmentAdapter(this, assignmentList, assignment -> {
            Toast.makeText(this, "Clicked: " + assignment.getTitle(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        loadAssignments();
    }

    private void loadAssignments() {
        SharedPreferences prefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        String studentGrade = prefs.getString("student_grade", null);
        if (studentGrade == null) {
            Toast.makeText(this, "Student not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("grade", studentGrade);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ASSIGNMENT_URL, requestBody,
                    response -> {
                        if ("success".equals(response.optString("status"))) {
                            assignmentList.clear();
                            JSONArray arr = response.optJSONArray("assignments");
                            if (arr != null) {
                                for (int i = 0; i < arr.length(); i++) {
                                    try {
                                        JSONObject obj = arr.getJSONObject(i);
                                        Assignment assignment = new Assignment(
                                                obj.getInt("id"),
                                                obj.getString("title"),
                                                obj.getString("description"),
                                                obj.getString("due_date")
                                        );
                                        assignmentList.add(assignment);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(this, "No assignments found", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Toast.makeText(this, "Error loading assignments", Toast.LENGTH_SHORT).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error building request", Toast.LENGTH_SHORT).show();
        }
    }
}
