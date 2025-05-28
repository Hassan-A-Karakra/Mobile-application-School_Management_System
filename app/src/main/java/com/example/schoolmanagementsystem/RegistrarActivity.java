package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrarActivity extends AppCompatActivity {

    RadioButton radioStudent, radioTeacher;
    EditText nameField, emailField, passwordField, gradeField;
    Spinner spinnerSubjects;
    Button registerBtn;
    String userType = "";
    String[] subjects = {"Math", "Science", "English", "Physics", "Chemistry", "Biology", "History"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        // ربط العناصر
        radioStudent = findViewById(R.id.radioStudent);
        radioTeacher = findViewById(R.id.radioTeacher);
        nameField = findViewById(R.id.editTextName);
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        gradeField = findViewById(R.id.editTextGrade);
        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        registerBtn = findViewById(R.id.buttonRegister);

        // إعداد Spinner المواد
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(adapter);

        // عند اختيار الطالب
        radioStudent.setOnClickListener(v -> {
            userType = "student";
            gradeField.setVisibility(View.VISIBLE);
            spinnerSubjects.setVisibility(View.GONE);
        });

        // عند اختيار المعلم
        radioTeacher.setOnClickListener(v -> {
            userType = "teacher";
            gradeField.setVisibility(View.GONE);
            spinnerSubjects.setVisibility(View.VISIBLE);
        });

        // عند الضغط على تسجيل
        registerBtn.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String pass = passwordField.getText().toString().trim();
            String grade = gradeField.getText().toString().trim();
            String subject = spinnerSubjects.getSelectedItem().toString(); // الحصول على المادة المختارة

            if (userType.isEmpty()) {
                Toast.makeText(this, "Please select user type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userType.equals("student") && grade.isEmpty()) {
                Toast.makeText(this, "Please enter grade", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userType.equals("teacher") && subject.isEmpty()) {
                Toast.makeText(this, "Please select subject", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // الانتقال حسب النوع
            if (userType.equals("student")) {
                startActivity(new Intent(RegistrarActivity.this, StudentLoginActivity.class));
            } else {
                startActivity(new Intent(RegistrarActivity.this, TeacherActivity.class));
            }
        });
    }
}
