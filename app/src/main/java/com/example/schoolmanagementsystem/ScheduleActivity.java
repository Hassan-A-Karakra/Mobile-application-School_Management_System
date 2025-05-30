package com.example.schoolmanagementsystem;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class ScheduleActivity extends AppCompatActivity {

    TextView scheduleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // ربط TextView لعرض الجدول
        scheduleTextView = findViewById(R.id.scheduleTextView);

        // بيانات افتراضية لعرض الجدول
        String schedule = "Monday: Math - 9:00 AM\nTuesday: Science - 10:00 AM\nWednesday: English - 11:00 AM\nThursday: History - 12:00 PM";

        // عرض الجدول في TextView
        scheduleTextView.setText(schedule);
    }
}
