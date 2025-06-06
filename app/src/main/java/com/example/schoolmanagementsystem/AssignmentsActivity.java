package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
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

        // ✅ Use correct shared preferences name
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
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
                        try {
                            if ("success".equals(response.optString("status"))) {
                                JSONArray assignments = response.getJSONArray("assignments");

                                for (int i = 0; i < assignments.length(); i++) {
                                    JSONObject obj = assignments.getJSONObject(i);
                                    String title = obj.getString("title");
                                    String description = obj.getString("description");
                                    String dueDate = obj.getString("due_date");

                                    TextView tv = new TextView(this);
                                    tv.setLayoutParams(new LinearLayout.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));
                                    tv.setText("Title: " + title + "\nDescription: " + description + "\nDue: " + dueDate);
                                    tv.setPadding(24, 24, 24, 24);
                                    assignmentContainer.addView(tv);
                                }
                            } else {
                                Toast.makeText(this, "No assignments found.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Error parsing assignments", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, "Failed to load assignments", Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(this).add(request);

        } catch (Exception e) {
            Toast.makeText(this, "Request error", Toast.LENGTH_SHORT).show();
        }
    }
}
