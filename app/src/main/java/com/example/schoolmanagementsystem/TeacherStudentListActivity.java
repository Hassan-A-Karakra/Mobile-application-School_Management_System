package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class TeacherStudentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<Student> studentList = new ArrayList<>();
    private Spinner spinnerGrade, spinnerSubject;
    private Button showStudentsBtn;

    private static final String BASE_URL = "http://10.0.2.2/student_system/";

    private static final String GRADES_SUBJECTS_URL = BASE_URL + "teacher_get_grades_subjects.php";
    private static final String STUDENTS_URL = BASE_URL + "teacher_view_students.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_student_list);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewStudentList);
        spinnerGrade = findViewById(R.id.spinnerGrade);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        showStudentsBtn = findViewById(R.id.buttonShowStudents);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this, studentList);
        recyclerView.setAdapter(adapter);

        // Load grades and subjects when activity starts
        loadGradesAndSubjects();

        // Setup button click listener
        showStudentsBtn.setOnClickListener(v -> loadStudentData());
    }

    private void loadGradesAndSubjects() {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                GRADES_SUBJECTS_URL,
                null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {

                            // Load grades
                            JSONArray gradesArray = response.getJSONArray("grades");
                            ArrayList<String> grades = new ArrayList<>();
                            grades.add("Select Grade");
                            for (int i = 0; i < gradesArray.length(); i++) {
                                grades.add(gradesArray.getString(i));
                            }
                            ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    grades
                            );
                            gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerGrade.setAdapter(gradeAdapter);

                            // Load subjects
                            JSONArray subjectsArray = response.getJSONArray("subjects");
                            ArrayList<String> subjects = new ArrayList<>();
                            subjects.add("Select Subject");
                            for (int i = 0; i < subjectsArray.length(); i++) {
                                subjects.add(subjectsArray.getString(i));
                            }
                            ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    subjects
                            );
                            subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerSubject.setAdapter(subjectAdapter);
                        } else {
                            Toast.makeText(this, "Failed to load grades and subjects: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error (grades/subjects): " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Volley error (grades/subjects): " + (error.getMessage() != null ? error.getMessage() : "Unknown error"), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void loadStudentData() {
        if (spinnerGrade.getSelectedItem() == null || spinnerSubject.getSelectedItem() == null) {
            Toast.makeText(this, "Please wait for grades and subjects to load, then select them.", Toast.LENGTH_SHORT).show();
            return;
        }

        String grade = spinnerGrade.getSelectedItem().toString();
        String subject = spinnerSubject.getSelectedItem().toString();

        if (grade.equals("Select Grade") || subject.equals("Select Subject") || grade.isEmpty() || subject.isEmpty()) {
            Toast.makeText(this, "Please select both grade and subject", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = STUDENTS_URL + "?grade=" + grade + "&subject=" + subject;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        studentList.clear();

                        if (response.getString("status").equals("success")) {
                            JSONArray studentsArray = response.getJSONArray("students");

                            for (int i = 0; i < studentsArray.length(); i++) {
                                JSONObject obj = studentsArray.getJSONObject(i);
                                Student student = new Student(
                                        obj.getInt("id"),
                                        obj.getString("name"),
                                        obj.getString("email"),
                                        obj.getInt("age")
                                );
                                studentList.add(student);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No students found for this grade and subject, or: " + response.getString("message"), Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error (students data): " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Volley error (students data): " + (error.getMessage() != null ? error.getMessage() : "Unknown network error"), Toast.LENGTH_LONG).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}