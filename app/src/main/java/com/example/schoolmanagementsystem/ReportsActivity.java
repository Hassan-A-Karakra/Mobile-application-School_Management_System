package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
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

        gradeContainer = findViewById(R.id.gradeContainer);
        buttonGenerateReport = findViewById(R.id.buttonGenerateReport);

        // ✅ Use consistent shared preferences
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("student_id", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Student not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        buttonGenerateReport.setOnClickListener(v -> loadGrades());
    }

    private void loadGrades() {
        String url = "http://10.0.2.2/student_system/get_grades.php";

        JSONObject postData = new JSONObject();
        try {
            postData.put("student_id", studentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData,
                response -> {
                    try {
                        if ("success".equals(response.optString("status"))) {
                            JSONArray grades = response.getJSONArray("grades");
                            gradeContainer.removeAllViews();

                            for (int i = 0; i < grades.length(); i++) {
                                JSONObject item = grades.getJSONObject(i);
                                String title = item.getString("title");
                                String grade = item.getString("grade");

                                TextView tv = new TextView(this);
                                tv.setLayoutParams(new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT));
                                tv.setText(title + ": " + grade);
                                tv.setTextSize(16);
                                tv.setPadding(24, 16, 24, 16);

                                gradeContainer.addView(tv);
                            }
                        } else {
                            Toast.makeText(this, "No grades found.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing grades", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to load grades", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
