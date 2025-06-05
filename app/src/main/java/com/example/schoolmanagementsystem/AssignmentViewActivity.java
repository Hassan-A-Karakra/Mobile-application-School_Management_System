package com.example.schoolmanagementsystem;

import android.content.Intent;
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

public class AssignmentViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AssignmentAdapter adapter;
    private List<Assignment> assignmentList;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);

        recyclerView = findViewById(R.id.recyclerViewAssignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assignmentList = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("student_id", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Student not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        adapter = new AssignmentAdapter(this, assignmentList, assignment -> {
            Intent intent = new Intent(this, SubmitAssignmentActivity.class);
            intent.putExtra("assignment_id", assignment.getId());
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        loadAssignments();
    }

    private void loadAssignments() {
        String url = "http://10.0.2.2/student_system/get_assignments.php";
        try {
            JSONObject postData = new JSONObject();
            postData.put("student_id", studentId);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                    response -> {
                        try {
                            if ("success".equals(response.optString("status"))) {
                                assignmentList.clear();
                                JSONArray arr = response.optJSONArray("assignments");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    Assignment assignment = new Assignment(
                                            obj.getInt("id"),
                                            obj.getString("title"),
                                            obj.getString("description"),
                                            obj.getString("due_date")
                                    );
                                    assignmentList.add(assignment);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Error parsing assignment data", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(this, "Failed to load assignments", Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            Toast.makeText(this, "Error building request", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
