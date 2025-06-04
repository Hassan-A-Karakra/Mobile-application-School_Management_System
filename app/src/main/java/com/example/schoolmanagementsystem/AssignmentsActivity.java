package com.example.schoolmanagementsystem;

import android.os.Bundle;
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

    LinearLayout assignmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        assignmentContainer = findViewById(R.id.assignmentContainer);
        loadAssignments();
    }

    private void loadAssignments() {
        String url = "http://10.0.2.2/student_system/get_assignments.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    if ("success".equals(response.optString("status"))) {
                        try {
                            JSONArray assignments = response.getJSONArray("assignments");
                            for (int i = 0; i < assignments.length(); i++) {
                                JSONObject a = assignments.getJSONObject(i);
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

                                assignmentContainer.addView(item);
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Error reading assignments", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> Toast.makeText(this, "Failed to load assignments", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
