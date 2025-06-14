package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    Spinner spinnerSubjects, spinnerDay, spinnerTime;
    Button registerBtn;
    String userType = "";
    String[] subjects = {"Select Subject", "Math", "Science", "English", "Arabic" };
    String[] days = {"Select Day", "Monday", "Tuesday", "Wednesday", "Thursday", "Sunday"};
    String[] times = {"Select Time", "08:00 AM", "09:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "01:00 PM"};

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
        spinnerDay = findViewById(R.id.spinnerDay);
        spinnerTime = findViewById(R.id.spinnerTime);
        registerBtn = findViewById(R.id.buttonRegister);

        ArrayAdapter<String> subjectsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        subjectsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(subjectsAdapter);

        ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, days);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(daysAdapter);

        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        timesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timesAdapter);

        radioStudent.setOnClickListener(v -> {
            userType = "student";
            gradeField.setVisibility(View.VISIBLE);
            ageField.setVisibility(View.VISIBLE);
            spinnerSubjects.setVisibility(View.VISIBLE);
            spinnerDay.setVisibility(View.GONE);
            spinnerTime.setVisibility(View.GONE);
        });

        radioTeacher.setOnClickListener(v -> {
            userType = "teacher";
            gradeField.setVisibility(View.VISIBLE);
            ageField.setVisibility(View.GONE);
            spinnerSubjects.setVisibility(View.VISIBLE);
            spinnerDay.setVisibility(View.VISIBLE);
            spinnerTime.setVisibility(View.VISIBLE);
        });


        registerBtn.setOnClickListener(v -> {
            String name = nameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String pass = passwordField.getText().toString().trim();
            String grade = gradeField.getText().toString().trim();
            String age = ageField.getText().toString().trim();
            String subject = "";
            String day = "";
            String time = "";

            if (userType.equals("teacher")) {
                subject = spinnerSubjects.getSelectedItem().toString();
                day = spinnerDay.getSelectedItem().toString();
                time = spinnerTime.getSelectedItem().toString();
            } else if (userType.equals("student")) {
                subject = spinnerSubjects.getSelectedItem().toString();
            }

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

            if (userType.equals("student") && (subject.isEmpty() || subject.equals("Select Subject"))) {
                Toast.makeText(this, "Please select a subject for the student", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userType.equals("teacher")) {
                if (subject.isEmpty() || subject.equals("Select Subject")) {
                    Toast.makeText(this, "Please select subject", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (day.isEmpty() || day.equals("Select Day")) {
                    Toast.makeText(this, "Please select day", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (time.isEmpty() || time.equals("Select Time")) {
                    Toast.makeText(this, "Please select time", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (grade.isEmpty()) {
                    Toast.makeText(this, "Please enter grade for teacher", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            sendRegistrationRequest(name, email, pass, grade, subject, age, day, time);
        });
    }

    private void sendRegistrationRequest(String name, String email, String password, String grade, String subject, String age, String day, String time) {
        String url = "http://10.0.2.2/student_system/" +
                (userType.equals("student") ? "register_student.php" : "register_teacher.php");

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                        if (jsonObject.getString("status").equals("success")) {
                            // Clear all input fields
                            nameField.setText("");
                            emailField.setText("");
                            passwordField.setText("");
                            gradeField.setText("");
                            ageField.setText("");
                            spinnerSubjects.setSelection(0);
                            spinnerDay.setSelection(0);
                            spinnerTime.setSelection(0);

                            // Send broadcast to refresh student list
                            if (userType.equals("student")) {
                                Intent refreshIntent = new Intent("STUDENT_ADDED");
                                sendBroadcast(refreshIntent);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Server response error: Invalid JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String errorData = new String(error.networkResponse.data);
                        Log.e("Volley Error", "Status Code: " + error.networkResponse.statusCode + ", Data: " + errorData);
                        Toast.makeText(this, "Server Error: " + error.networkResponse.statusCode + " " + errorData, Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("Volley Error", "Network Error: " + error.getMessage(), error);
                        Toast.makeText(this, "Network Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
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
                    params.put("subject", subject);
                } else {
                    params.put("subject", subject);
                    params.put("day", day);
                    params.put("time", time);
                    params.put("grade", grade);
                }
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}