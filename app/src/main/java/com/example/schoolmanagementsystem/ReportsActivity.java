package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReportsActivity extends AppCompatActivity {

    private int studentId;
    private LinearLayout gradeContainer;
    private Button buttonGenerateReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        studentId = getIntent().getIntExtra("student_id", -1);
        gradeContainer = findViewById(R.id.gradeContainer);
        buttonGenerateReport = findViewById(R.id.buttonGenerateReport);

        if (studentId == -1) {
            Toast.makeText(this, "Student ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        buttonGenerateReport.setOnClickListener(v -> {
            loadGrades();
        });
    }

    private void loadGrades() {
        String url = "http://10.0.2.2/student_system/get_grades.php?student_id=" + studentId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    if ("success".equals(response.optString("status"))) {
                        try {
                            gradeContainer.removeAllViews();  // Clear old content
                            JSONArray grades = response.getJSONArray("grades");

                            for (int i = 0; i < grades.length(); i++) {
                                JSONObject g = grades.getJSONObject(i);
                                String title = g.getString("title");
                                String grade = g.getString("grade");

                                TextView item = new TextView(this);
                                item.setText("📘 " + title + ": " + grade);
                                item.setTextSize(16);
                                item.setPadding(12, 12, 12, 12);
                                gradeContainer.addView(item);
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Error reading grades", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> Toast.makeText(this, "Failed to load grades", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
