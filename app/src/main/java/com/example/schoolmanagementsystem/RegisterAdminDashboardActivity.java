package com.example.schoolmanagementsystem;
import android.content.Intent;

import android.os.Bundle;

import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


public class RegisterAdminDashboardActivity extends AppCompatActivity {

    Button addTeacherBtn, viewStudentsBtn, viewTeachersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin_dashboard);

        addTeacherBtn = findViewById(R.id.addTeacherBtn);
        viewStudentsBtn = findViewById(R.id.viewStudentsBtn);
        viewTeachersBtn = findViewById(R.id.viewTeachersBtn);

        addTeacherBtn.setOnClickListener(v -> startActivity(new Intent(this, RegistrarActivity.class)));
        viewStudentsBtn.setOnClickListener(v -> startActivity(new Intent(this, RegisterStudentListActivity.class)));
        viewTeachersBtn.setOnClickListener(v -> startActivity(new Intent(this, RegisterTeacherListActivity.class)));
    }
}

