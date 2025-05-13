package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // إعداد الحواف (Insets) لعرض الأشرطة العلوية والسفلية
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // إضافة التفاعل مع زر الحضور
        Button buttonMarkAttendance = findViewById(R.id.buttonMarkAttendance);
        buttonMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // الكود الذي سيتم تنفيذه عند الضغط على زر الحضور
                Toast.makeText(AttendanceActivity.this, "Attendance Marked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
