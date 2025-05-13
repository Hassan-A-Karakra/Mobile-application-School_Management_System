package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherActivity extends AppCompatActivity {

    Button buttonStudentList, buttonGradeInput, buttonAttendance, buttonReports, buttonCommunicate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        // ربط الأزرار الخاصة بكل خيار
        buttonStudentList = findViewById(R.id.buttonStudentList);
        buttonGradeInput = findViewById(R.id.buttonGradeInput);
        buttonAttendance = findViewById(R.id.buttonAttendance);
        buttonReports = findViewById(R.id.buttonReports);
        buttonCommunicate = findViewById(R.id.buttonCommunicate);

        // عند الضغط على زر "قائمة الطلاب"
        buttonStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, StudentListActivity.class);
                startActivity(intent);
            }
        });

        // عند الضغط على زر "إدخال الدرجات"
        buttonGradeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, GradeInputActivity.class);
                startActivity(intent);
            }
        });

        // عند الضغط على زر "إدارة الحضور"
        buttonAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, AttendanceActivity.class);
                startActivity(intent);
            }
        });

        // عند الضغط على زر "التقارير"
        buttonReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, ReportsActivity.class);
                startActivity(intent);
            }
        });

        // عند الضغط على زر "التواصل مع الطلاب"
        buttonCommunicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherActivity.this, CommunicateActivity.class);
                startActivity(intent);
            }
        });
    }
}
