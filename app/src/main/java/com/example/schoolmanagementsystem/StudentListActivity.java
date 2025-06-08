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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<Student> studentList = new ArrayList<>();
    private Spinner spinnerClass, spinnerSubject;
    private Button showStudentsBtn;
    private ArrayAdapter<String> classAdapter, subjectAdapter;
    private ArrayList<String> classList = new ArrayList<>();
    private ArrayList<String> subjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        recyclerView = findViewById(R.id.recyclerViewStudentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this, studentList);
        recyclerView.setAdapter(adapter);

        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        showStudentsBtn = findViewById(R.id.buttonShowStudents);

        classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classAdapter);

        subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectList);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

        fetchGrades();
        fetchSubjects();

        showStudentsBtn.setOnClickListener(v -> {
            String selectedClass = (String) spinnerClass.getSelectedItem();
            String selectedSubject = (String) spinnerSubject.getSelectedItem();
            if (selectedClass != null && selectedSubject != null) {
                loadStudentData(selectedClass, selectedSubject);
            } else {
                Toast.makeText(this, "اختر الصف والمادة", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchGrades() {
        String url = "http://10.0.2.2/student_system/get_grades.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    classList.clear();
                    JSONArray grades = response.optJSONArray("grades");
                    if (grades != null) {
                        for (int i = 0; i < grades.length(); i++) {
                            classList.add(grades.optString(i));
                        }
                        classAdapter.notifyDataSetChanged();
                    }
                },
                error -> Toast.makeText(this, "Failed to fetch grades", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void fetchSubjects() {
        String url = "http://10.0.2.2/student_system/get_subjects.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    subjectList.clear();
                    JSONArray subjects = response.optJSONArray("subjects");
                    if (subjects != null) {
                        for (int i = 0; i < subjects.length(); i++) {
                            subjectList.add(subjects.optString(i));
                        }
                        subjectAdapter.notifyDataSetChanged();
                    }
                },
                error -> Toast.makeText(this, "Failed to fetch subjects", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }

    private void loadStudentData(String grade, String subject) {
        String url = "http://10.0.2.2/student_system/view_students.php?grade=" + grade + "&subject=" + subject;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    studentList.clear();
                    if ("success".equals(response.optString("status"))) {
                        JSONArray students = response.optJSONArray("students");
                        for (int i = 0; i < students.length(); i++) {
                            JSONObject obj = null;
                            try {
                                obj = students.getJSONObject(i);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Student student = null;
                            try {
                                student = new Student(
                                        obj.getInt("id"),
                                        obj.getString("name"),
                                        obj.getString("email"),
                                        obj.getInt("age")
                                );
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            studentList.add(student);
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Failed to fetch students", Toast.LENGTH_SHORT).show()
        );
        Volley.newRequestQueue(this).add(request);
    }
}