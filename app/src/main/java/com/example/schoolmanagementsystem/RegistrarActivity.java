package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {

    RadioButton radioStudent, radioTeacher;
    EditText nameField, emailField, passwordField, gradeField, ageField;
    Spinner spinnerSubjects;
    Button registerBtn;
    String userType = "";
    String[] subjects = {"Math", "Science", "English", "Physics", "Chemistry", "Biology", "History"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

         radioStudent = findViewById(R.id.radioStudent);
        radioTeacher = findViewById(R.id.radioTeacher);
        nameField = findViewById(R.id.editTextName);
        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        gradeField = findViewById(R.id.editTextGrade);
        ageField = findViewById(R.id.editTextAge);
        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        registerBtn = findViewById(R.id.buttonRegister);

         ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(adapter);

         radioStudent.setOnClickListener(v -> {
            userType = "student";
            gradeField.setVisibility(View.VISIBLE);
            ageField.setVisibility(View.VISIBLE);
            spinnerSubjects.setVisibility(View.GONE);
        });

         radioTeacher.setOnClickListener(v -> {
            userType = "teacher";
            gradeField.setVisibility(View.GONE);
            ageField.setVisibility(View.GONE);
            spinnerSubjects.setVisibility(View.VISIBLE);
        });

         registerBtn.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String pass = passwordField.getText().toString().trim();
            String grade = gradeField.getText().toString().trim();
            String age = ageField.getText().toString().trim();
            String subject = spinnerSubjects.getSelectedItem().toString();

            if (userType.isEmpty()) {
                Toast.makeText(this, "Please select user type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userType.equals("student") && (grade.isEmpty() || age.isEmpty())) {
                Toast.makeText(this, "Please enter grade and age", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userType.equals("teacher") && subject.isEmpty()) {
                Toast.makeText(this, "Please select subject", Toast.LENGTH_SHORT).show();
                return;
            }

            sendRegistrationRequest(name, email, pass, grade, subject, age);
        });
    }

    private void sendRegistrationRequest(String name, String email, String password, String grade, String subject, String age) {
        String url = "http://10.0.2.2/student_system/" +
                (userType.equals("student") ? "register_student.php" : "register_teacher.php");

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                        if (jsonObject.getString("status").equals("success")) {
                            if (userType.equals("student")) {
                                startActivity(new Intent(RegistrarActivity.this, StudentActivity.class));
                            } else {
                                startActivity(new Intent(RegistrarActivity.this, TeacherActivity.class));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Server response error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                if (userType.equals("student")) {
                    params.put("grade", grade);
                    params.put("age", age);
                } else {
                    params.put("subject", subject);
                }
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
