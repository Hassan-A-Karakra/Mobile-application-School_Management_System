package com.example.schoolmanagementsystem;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate; // Still used to send date to server, but server won't use it for attendance table anymore
import java.util.ArrayList;
import java.util.List;

public class TeacherAttendanceActivity extends AppCompatActivity {
    private Spinner spinnerGrade;
    private Spinner spinnerSubject;
    private RecyclerView attendanceRecyclerView;
    private TeacherAttendanceAdapter adapter;
    private Button buttonSubmit;
    private List<Student> studentList;

    private static final String API_URL = "http://10.0.2.2/student_system/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance);

        // Initialize views
        spinnerGrade = findViewById(R.id.spinnerGrade);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Setup grade spinner
        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(gradeAdapter);

        // Initialize student list and adapter
        studentList = new ArrayList<>();
        adapter = new TeacherAttendanceAdapter(studentList);
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceRecyclerView.setAdapter(adapter);

        // Load grades
        loadGrades();

        // Setup listeners
        spinnerGrade.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                fetchSubjects();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        spinnerSubject.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                fetchStudents();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        buttonSubmit.setOnClickListener(v -> saveAttendance());
    }

    private void loadGrades() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                API_URL + "teacher_get_grades.php", null,
                response -> {
                    List<String> grades = new ArrayList<>();
                    grades.add("Select Class");
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            grades.add(response.getString(i));
                        }
                        ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, grades);
                        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerGrade.setAdapter(gradeAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TeacherAttendance", "JSON parsing error in loadGrades: " + e.getMessage());
                        Toast.makeText(this, "Error parsing grades data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("TeacherAttendance", "Volley error in loadGrades: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"));
                    Toast.makeText(this, "Error loading grades", Toast.LENGTH_SHORT).show();
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void fetchSubjects() {
        String grade = spinnerGrade.getSelectedItem().toString();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                API_URL + "teacher_get_subjects.php", null,
                response -> {
                    List<String> subjects = new ArrayList<>();
                    subjects.add("Select Subject");
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            subjects.add(response.getString(i));
                        }
                        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, subjects);
                        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerSubject.setAdapter(subjectAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TeacherAttendance", "JSON parsing error in fetchSubjects: " + e.getMessage());
                        Toast.makeText(this, "Error parsing subjects data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("TeacherAttendance", "Volley error in fetchSubjects: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"));
                    Toast.makeText(this, "Error loading subjects", Toast.LENGTH_SHORT).show();
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void fetchStudents() {
        String selectedGrade = spinnerGrade.getSelectedItem().toString();
        String selectedSubject = spinnerSubject.getSelectedItem().toString();

        // Add validation for selected class and subject
        if (selectedGrade.equals("Select Class") || selectedSubject.equals("Select Subject")) {
            Toast.makeText(this, "Please select both a class and a subject.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("TeacherAttendance", "Selected Grade: " + selectedGrade);
        Log.d("TeacherAttendance", "Selected Subject: " + selectedSubject);

        String url = API_URL + "teacher_attendance_get_students_by_class_and_subject.php?class_name=" + selectedGrade + "&subject=" + selectedSubject;

        Log.d("TeacherAttendance", "Request URL: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                response -> {
                    studentList.clear();
                    try {
                        Log.d("TeacherAttendance", "JSON Response for students: " + response.toString());

                        if (response.has("success") && response.getBoolean("success")) {
                            JSONArray studentsArray = response.getJSONArray("students");
                            for (int i = 0; i < studentsArray.length(); i++) {
                                JSONObject obj = studentsArray.getJSONObject(i);
                                Student student = new Student(
                                        obj.getInt("id"),
                                        obj.getString("name"),
                                        obj.getString("email"),
                                        obj.getInt("age"),
                                        selectedGrade
                                );
                                // absence_count will now come as a cumulative value directly
                                if (obj.has("absence_count")) {
                                    student.setAbsenceCount(obj.getInt("absence_count"));
                                }
                                studentList.add(student);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            String message = response.has("message") ? response.getString("message") : "Unknown error from server.";
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            Log.e("TeacherAttendance", "Server returned success: false. Message: " + message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("TeacherAttendance", "JSON parsing error: " + e.getMessage());
                    }
                },
                error -> {
                    String errorMessage = "Unknown error";
                    if (error.networkResponse != null) {
                        errorMessage = new String(error.networkResponse.data);
                        Log.e("TeacherAttendance", "Volley network error data (fetchStudents): " + errorMessage);
                    } else if (error.getMessage() != null) {
                        errorMessage = error.getMessage();
                    }
                    Toast.makeText(this, "Failed to fetch students: " + errorMessage, Toast.LENGTH_LONG).show();
                    Log.e("TeacherAttendance", "Volley error (fetchStudents): " + errorMessage);
                }
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void saveAttendance() {
        String subject = spinnerSubject.getSelectedItem().toString();
        String grade = spinnerGrade.getSelectedItem().toString(); // Get grade for validation

        // Add validation for selected class and subject before saving
        if (subject.equals("Select Subject") || grade.equals("Select Class")) {
            Toast.makeText(this, "Please select both a class and a subject before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("subject", subject);
            // Date is no longer used for the attendance table, but we send it as a precaution for future uses or other logs
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestBody.put("date", LocalDate.now().toString());
            } else {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                requestBody.put("date", sdf.format(new java.util.Date()));
            }

            JSONArray attendance = new JSONArray();
            for (Student student : studentList) {
                JSONObject record = new JSONObject();
                record.put("student_id", student.getId());
                record.put("is_present", student.isPresent());
                // We no longer send individual absence_count as the server handles the cumulative increment
                attendance.put(record);
            }
            requestBody.put("attendance", attendance);

            Log.d("TeacherAttendance", "Sending attendance data: " + requestBody.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    API_URL + "teacher_save_attendance.php", requestBody,
                    response -> {
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(this, "Attendance saved successfully", Toast.LENGTH_SHORT).show();
                                // Re-fetch students to update the total (cumulative) absence counts
                                fetchStudents();
                            } else {
                                String message = response.has("message") ? response.getString("message") : "Unknown error from server.";
                                Toast.makeText(this, "Error saving attendance: " + message, Toast.LENGTH_LONG).show();
                                Log.e("TeacherAttendance", "Server returned success: false. Message: " + message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error reading server response", Toast.LENGTH_SHORT).show();
                            Log.e("TeacherAttendance", "JSON parsing error in saveAttendance response: " + e.getMessage());
                        }
                    },
                    error -> {
                        String errorMessage = "Unknown error";
                        if (error.networkResponse != null) {
                            errorMessage = new String(error.networkResponse.data);
                            Log.e("TeacherAttendance", "Volley network error data: " + errorMessage);
                        } else if (error.getMessage() != null) {
                            errorMessage = error.getMessage();
                        }
                        Toast.makeText(this, "Error saving attendance: " + errorMessage, Toast.LENGTH_LONG).show();
                        Log.e("TeacherAttendance", "Volley error in saveAttendance: " + errorMessage);
                    }
            );
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error setting up request data", Toast.LENGTH_SHORT).show();
            Log.e("TeacherAttendance", "JSON creation error in saveAttendance: " + e.getMessage());
        }
    }
}