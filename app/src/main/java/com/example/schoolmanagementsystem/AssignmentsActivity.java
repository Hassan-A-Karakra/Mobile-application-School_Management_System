package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

public class AssignmentsActivity extends AppCompatActivity {

    private LinearLayout assignmentContainer;
    private static final String ASSIGNMENT_URL = "http://10.0.2.2/student_system/get_assignments.php";
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        assignmentContainer = findViewById(R.id.assignmentContainer);

        SharedPreferences prefs = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("student_id", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Student not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        loadAssignments();
    }

    private void loadAssignments() {
        assignmentContainer.removeAllViews();

        try {
            JSONObject postData = new JSONObject();
            postData.put("student_id", studentId);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ASSIGNMENT_URL, postData,
                    response -> {
                        if ("success".equals(response.optString("status"))) {
                            try {
                                JSONArray assignments = response.getJSONArray("assignments");

                                if (assignments.length() == 0) {
                                    TextView noAssign = new TextView(this);
                                    noAssign.setText("🎉 No pending assignments");
                                    noAssign.setTextSize(18);
                                    assignmentContainer.addView(noAssign);
                                }

                                for (int i = 0; i < assignments.length(); i++) {
                                    JSONObject a = assignments.getJSONObject(i);
                                    int id = a.getInt("id");
                                    String title = a.getString("title");
                                    String desc = a.getString("description");
                                    String due = a.getString("due_date");

                                    TextView item = new TextView(this);
                                    item.setText("📘 " + title + "\n" + desc + "\nDue: " + due);
                                    item.setTextSize(16);
                                    item.setPadding(24, 24, 24, 24);
                                    item.setBackgroundResource(R.drawable.card_background);

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(0, 0, 0, 24);
                                    item.setLayoutParams(params);

                                    final int assignmentId = id;
                                    item.setOnClickListener(v -> {
                                        Intent intent = new Intent(this, SubmitAssignmentActivity.class);
                                        intent.putExtra("assignment_id", assignmentId);
                                        startActivity(intent);
                                    });

                                    assignmentContainer.addView(item);
                                }

                            } catch (Exception e) {
                                Toast.makeText(this, "Error reading data", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    error -> Toast.makeText(this, "Error loading assignments", Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(this).add(request);

        } catch (Exception e) {
            Toast.makeText(this, "Error building request", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAssignments(); // Refresh assignments when returning from submission
    }
}
