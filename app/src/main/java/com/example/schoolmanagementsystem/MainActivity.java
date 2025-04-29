package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonRegistrar, buttonTeacher, buttonStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ربط الأزرار مع الكود
        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        buttonTeacher = findViewById(R.id.buttonTeacher);
        buttonStudent = findViewById(R.id.buttonStudent);

        // عند الضغط على زر تسجيل الدخول كمشرف
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // الانتقال إلى النشاط الخاص بالمشرف
                Intent intent = new Intent(MainActivity.this, RegistrarActivity.class);
                startActivity(intent);
            }
        });

        // عند الضغط على زر تسجيل الدخول كمعلم
        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // الانتقال إلى النشاط الخاص بالمعلم
                Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
                startActivity(intent);
            }
        });

        // عند الضغط على زر تسجيل الدخول كطالب
        buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // الانتقال إلى النشاط الخاص بالطالب
                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                startActivity(intent);
            }
        });
    }
}
