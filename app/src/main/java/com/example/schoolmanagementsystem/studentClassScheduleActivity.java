package com.example.schoolmanagementsystem;

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
import org.json.JSONObject;
import java.util.*;

public class studentClassScheduleActivity extends AppCompatActivity {

    private static final String TAG = "ClassScheduleActivity";
    private RecyclerView dayRecyclerView;
    private static final String BASE_URL = "http://10.0.2.2/student_system/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class_schedule);

        dayRecyclerView = findViewById(R.id.dayRecyclerView);
        dayRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get student ID from intent or SharedPreferences
        int studentId = getIntent().getIntExtra("student_id", -1);
        if (studentId == -1) {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            studentId = prefs.getInt("student_id", -1);
        }
        if (studentId == -1) {
            Toast.makeText(this, "Invalid student ID. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadSchedule(studentId);
    }

    private void loadSchedule(int studentId) {
        String url = BASE_URL + "student_get_schedule.php?student_id=" + studentId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if ("success".equals(response.optString("status"))) {
                            JSONArray schedule = response.getJSONArray("schedule");
                            // Group by day
                            Map<String, List<StudentClassSession>> dayMap = new LinkedHashMap<>();
                            for (int i = 0; i < schedule.length(); i++) {
                                JSONObject row = schedule.getJSONObject(i);
                                String day = row.getString("day");
                                String subject = row.getString("subject");
                                String time = row.getString("time");
                                String teacher = row.has("teacher") && !row.isNull("teacher") ? row.getString("teacher") : "";
                                if (!dayMap.containsKey(day)) {
                                    dayMap.put(day, new ArrayList<>());
                                }
                                dayMap.get(day).add(new StudentClassSession(subject, time, teacher));
                            }
                            List<StudentDaySchedule> studentDaySchedules = new ArrayList<>();
                            for (Map.Entry<String, List<StudentClassSession>> entry : dayMap.entrySet()) {
                                studentDaySchedules.add(new StudentDaySchedule(entry.getKey(), entry.getValue()));
                            }
                            dayRecyclerView.setAdapter(new StudentDayScheduleAdapter(studentDaySchedules));
                        } else {
                            String errorMessage = response.optString("message", "Unknown error occurred");
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing schedule", e);
                        Toast.makeText(this, "Error reading schedule: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Network error", error);
                    Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}