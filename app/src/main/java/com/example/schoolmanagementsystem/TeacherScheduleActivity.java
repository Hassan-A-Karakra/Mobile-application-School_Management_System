package com.example.schoolmanagementsystem;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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

public class TeacherScheduleActivity extends AppCompatActivity {

    private static final String TAG = "TeacherScheduleActivity";
    private LinearLayout scheduleContainer;

    private static final String BASE_URL = "http://10.0.2.2/student_system/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_schedule);

        scheduleContainer = findViewById(R.id.scheduleContainer);

        int teacherId = getIntent().getIntExtra("teacher_id", -1);
        if (teacherId == -1) {
            showError("Invalid teacher ID. Please provide a valid teacher ID.");
            return;
        }

         fetchTeacherSchedule(teacherId);
    }

    private void fetchTeacherSchedule(int teacherId) {

        String url = BASE_URL + "teacher_get_teacher_schedule.php?teacher_id=" + teacherId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if ("success".equals(response.optString("status"))) {
                            JSONArray schedule = response.getJSONArray("schedule");
                            scheduleContainer.removeAllViews();

                            for (int i = 0; i < schedule.length(); i++) {
                                JSONObject row = schedule.getJSONObject(i);
                                String subject = row.getString("subject");
                                String day = row.getString("day");
                                String time = row.getString("time");
                                String grade = row.getString("grade");

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

                                TextView tvSubject = new TextView(this);
                                tvSubject.setText(day + ": " + subject);
                                tvSubject.setTextSize(16);
                                tvSubject.setTypeface(null, Typeface.BOLD);
                                tvSubject.setTextColor(0xFF000000);
                                card.addView(tvSubject);

                                TextView tvTime = new TextView(this);
                                tvTime.setText("Time: " + time);
                                tvTime.setTextSize(14);
                                tvTime.setTextColor(0xFF555555);
                                card.addView(tvTime);

                                TextView tvGrade = new TextView(this);
                                tvGrade.setText("Grade: " + grade);
                                tvGrade.setTextSize(14);
                                tvGrade.setTextColor(0xFF555555);
                                card.addView(tvGrade);

                                scheduleContainer.addView(card);
                            }
                        } else {

                            showError(response.optString("message", "No schedule found."));
                        }
                    } catch (Exception e) {

                        showError("Error processing schedule data.");
                        Log.e(TAG, "Parse error: ", e);
                    }
                },
                error -> {

                    showError("Failed to load schedule. Check your internet connection or server.");
                    Log.e(TAG, "Volley error: ", error);
                });


        Volley.newRequestQueue(this).add(request);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}