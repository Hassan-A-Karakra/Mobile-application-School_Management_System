package com.example.schoolmanagementsystem;

import android.os.Build;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
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
    private static final String[] GRADES = {"10", "11", "12"};

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
                android.R.layout.simple_spinner_item, GRADES);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(gradeAdapter);

        // Initialize student list and adapter
        studentList = new ArrayList<>();
        adapter = new TeacherAttendanceAdapter(studentList);
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceRecyclerView.setAdapter(adapter);

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

    private void fetchSubjects() {
        String grade = spinnerGrade.getSelectedItem().toString();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                API_URL + "get_subjects.php?grade=" + grade, null,
                response -> {
                    List<String> subjects = new ArrayList<>();
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
                    }
                },
                error -> Toast.makeText(this, "Error loading subjects", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void fetchStudents() {
        String grade = spinnerGrade.getSelectedItem().toString();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                API_URL + "get_students_by_grade.php?grade=" + grade, null,
                response -> {
                    studentList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                          //  Student student = new Student();
                          //  student.setId(obj.getInt("id"));
                          //  student.setName(obj.getString("name"));
                           // studentList.add(student);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void saveAttendance() {
        String subject = spinnerSubject.getSelectedItem().toString();
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("subject", subject);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestBody.put("date", LocalDate.now().toString());
            }

            JSONArray attendance = new JSONArray();
            for (Student student : studentList) {
                JSONObject record = new JSONObject();
                record.put("student_id", student.getId());
               // record.put("is_present", student.isPresent());
              //  record.put("absence_count", student.getAbsenceCount());
                attendance.put(record);
            }
            requestBody.put("attendance", attendance);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    API_URL + "save_attendance.php", requestBody,
                    response -> {
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(this, "Attendance saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Error saving attendance", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(this, "Error saving attendance", Toast.LENGTH_SHORT).show()
            );
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}