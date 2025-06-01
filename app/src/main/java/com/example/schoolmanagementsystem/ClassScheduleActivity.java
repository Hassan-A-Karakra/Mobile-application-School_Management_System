
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
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;

public class ClassScheduleActivity extends AppCompatActivity {

    int studentId;
    LinearLayout scheduleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        scheduleContainer = findViewById(R.id.scheduleContainer);
        studentId = getIntent().getIntExtra("student_id", -1);

        if (studentId != -1) {
            loadSchedule();
        } else {
            Toast.makeText(this, "Student ID missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSchedule() {
        String url = "http://10.0.2.2/student_system/get_schedule.php?student_id=" + studentId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    if ("success".equals(response.optString("status"))) {
                        try {
                            JSONArray schedule = response.getJSONArray("schedule");
                            for (int i = 0; i < schedule.length(); i++) {
                                JSONObject item = schedule.getJSONObject(i);
                                String subject = item.getString("subject");
                                String start = item.getString("start_time");
                                String end = item.getString("end_time");
                                String teacher = item.getString("teacher_name");
                                String day = item.getString("day");

                                // Create card layout
                                LinearLayout card = new LinearLayout(this);
                                card.setOrientation(LinearLayout.VERTICAL);
                                card.setPadding(32, 24, 32, 24);
                                card.setBackgroundResource(R.drawable.card_background);

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT
                                );
                                layoutParams.setMargins(0, 0, 0, 24);
                                card.setLayoutParams(layoutParams);

                                // Subject
                                TextView tvSubject = new TextView(this);
                                tvSubject.setText(day + ": " + subject + " - " + start + " to " + end);
                                tvSubject.setTextSize(16);
                                tvSubject.setTypeface(null, Typeface.BOLD);
                                tvSubject.setTextColor(0xFF000000);
                                card.addView(tvSubject);

                                // Teacher
                                TextView tvTeacher = new TextView(this);
                                tvTeacher.setText(teacher);
                                tvTeacher.setTextSize(14);
                                tvTeacher.setTextColor(0xFF555555);
                                card.addView(tvTeacher);

                                scheduleContainer.addView(card);
                            }
                        } catch (Exception e) {
                            Toast.makeText(this, "Error parsing schedule", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> Toast.makeText(this, "Failed to load schedule", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}
