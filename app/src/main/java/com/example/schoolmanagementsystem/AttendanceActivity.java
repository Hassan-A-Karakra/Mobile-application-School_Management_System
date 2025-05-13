package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private Button buttonReports;
    private TextView textViewReport;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // ربط العناصر من XML
        buttonReports = findViewById(R.id.buttonReports);
        textViewReport = findViewById(R.id.textViewReport);  // تأكد من إضافة TextView في XML لعرض التقرير

        // إعداد بيانات الطلاب (يمكنك جلبها من قاعدة بيانات أو API)
        studentList = new ArrayList<>();
        studentList.add(new Student("Student 1", true, "A"));
        studentList.add(new Student("Student 2", false, "B"));
        studentList.add(new Student("Student 3", true, "A+"));
        studentList.add(new Student("Student 4", false, "C"));

        // عندما يتم الضغط على زر التقارير
        buttonReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // إنشاء التقرير
                StringBuilder report = new StringBuilder();
                report.append("Student Report:\n\n");

                // بناء التقرير بناءً على بيانات الطلاب
                for (Student student : studentList) {
                    report.append("Name: ").append(student.getName())
                            .append("\nAttendance: ").append(student.isPresent() ? "Present" : "Absent")
                            .append("\nGrade: ").append(student.getGrade())
                            .append("\n\n");
                }

                // عرض التقرير في TextView
                textViewReport.setText(report.toString());

                // عرض Toast لتأكيد إنشاء التقرير
                Toast.makeText(AttendanceActivity.this, "Report Generated!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
