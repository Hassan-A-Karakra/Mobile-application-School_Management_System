package com.example.schoolmanagementsystem;
import android.content.Intent;

import android.os.Bundle;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


public class AdminDashboardActivity extends AppCompatActivity {

    Button addStudentBtn, addTeacherBtn, viewStudentsBtn, viewTeachersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        addStudentBtn = findViewById(R.id.addStudentBtn);
        addTeacherBtn = findViewById(R.id.addTeacherBtn);
        viewStudentsBtn = findViewById(R.id.viewStudentsBtn);
        viewTeachersBtn = findViewById(R.id.viewTeachersBtn);

        addStudentBtn.setOnClickListener(v -> startActivity(new Intent(this, RegistrarActivity.class)));
        addTeacherBtn.setOnClickListener(v -> startActivity(new Intent(this, RegistrarActivity.class)));
        viewStudentsBtn.setOnClickListener(v -> startActivity(new Intent(this, StudentListActivity.class)));
        viewTeachersBtn.setOnClickListener(v -> startActivity(new Intent(this, TeacherListActivity.class)));
    }
}

