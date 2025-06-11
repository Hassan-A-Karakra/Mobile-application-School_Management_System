package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherGradeInputActivity extends AppCompatActivity {

    private Spinner spinnerClass, spinnerSubject;
    private RecyclerView recyclerViewStudents;
    private Button buttonShareGrades;
    private List<Student> studentList;
    private TeacherStudentGradeAdapter teacherStudentGradeAdapter;

    private RequestQueue requestQueue;
    private static final String BASE_URL = "http://10.0.2.2/student_system/";

    private int teacherId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_grade_input);

        requestQueue = Volley.newRequestQueue(this);

        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        buttonShareGrades = findViewById(R.id.buttonShareGrades);

        studentList = new ArrayList<>();
        teacherStudentGradeAdapter = new TeacherStudentGradeAdapter(studentList);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStudents.setAdapter(teacherStudentGradeAdapter);

        fetchClasses();
        fetchSubjects();

        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && spinnerSubject.getSelectedItemPosition() > 0) {
                    fetchStudents();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && spinnerClass.getSelectedItemPosition() > 0) {
                    fetchStudents();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonShareGrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGrades(true);
            }
        });
    }

    private void fetchClasses() {
        String url = BASE_URL + "teacher_get_grades.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> classes = new ArrayList<>();
                        classes.add("Select Class");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                classes.add(response.getString(i));
                            } catch (JSONException e) {
                                Log.e("Volley", "Error parsing class: " + e.getMessage());
                            }
                        }
                        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(TeacherGradeInputActivity.this,
                                android.R.layout.simple_spinner_item, classes);
                        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerClass.setAdapter(classAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error fetching classes: " + error.getMessage());
                        Toast.makeText(TeacherGradeInputActivity.this, 
                            "Failed to fetch classes: " + error.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void fetchSubjects() {
        String url = BASE_URL + "teacher_get_subjects.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> subjects = new ArrayList<>();
                        subjects.add("Select Subject");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                subjects.add(response.getString(i));
                            } catch (JSONException e) {
                                Log.e("Volley", "Error parsing subject: " + e.getMessage());
                            }
                        }
                        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(TeacherGradeInputActivity.this,
                                android.R.layout.simple_spinner_item, subjects);
                        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerSubject.setAdapter(subjectAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error fetching subjects: " + error.getMessage());
                        Toast.makeText(TeacherGradeInputActivity.this, 
                            "Failed to fetch subjects: " + error.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonArrayRequest);
    }

    private void fetchStudents() {
        String selectedClass = spinnerClass.getSelectedItem().toString();
        String selectedSubject = spinnerSubject.getSelectedItem().toString();

        if (selectedClass.equals("Select Class") || selectedSubject.equals("Select Subject")) {
            Toast.makeText(this, "Please select class and subject", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = BASE_URL + "teacher_get_students_by_class_and_subject.php?class_name=" + selectedClass + "&subject=" + selectedSubject;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("success") && response.getBoolean("success")) {
                                studentList.clear();
                                if (response.has("students")) {
                                    JSONArray studentsJson = response.getJSONArray("students");
                                    for (int i = 0; i < studentsJson.length(); i++) {
                                        try {
                                            JSONObject studentJson = studentsJson.getJSONObject(i);
                                            int id = studentJson.getInt("id");
                                            String name = studentJson.getString("name");
                                            String email = studentJson.optString("email", "");
                                            int age = studentJson.optInt("age", 0);
                                            String score = studentJson.optString("score", "");
                                            studentList.add(new Student(id, name, email, age, score));
                                        } catch (JSONException e) {
                                            Log.e("Volley", "Error parsing student at index " + i + ": " + e.getMessage());
                                        }
                                    }
                                    teacherStudentGradeAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(TeacherGradeInputActivity.this, 
                                        "No students found", 
                                        Toast.LENGTH_SHORT).show();
                                    studentList.clear();
                                    teacherStudentGradeAdapter.notifyDataSetChanged();
                                }
                            } else {
                                String message = response.optString("message", "Unknown error occurred");
                                Toast.makeText(TeacherGradeInputActivity.this, message, Toast.LENGTH_SHORT).show();
                                studentList.clear();
                                teacherStudentGradeAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            Log.e("Volley", "Error parsing response: " + e.getMessage());
                            Toast.makeText(TeacherGradeInputActivity.this, 
                                "Error processing server response", 
                                Toast.LENGTH_SHORT).show();
                            studentList.clear();
                            teacherStudentGradeAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Server connection error";
                        if (error.networkResponse != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "UTF-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                errorMessage = jsonObject.optString("message", errorMessage);
                            } catch (Exception e) {
                                Log.e("Volley", "Error parsing error response: " + e.getMessage());
                            }
                        }
                        Log.e("Volley", "Error fetching students: " + error.getMessage());
                        Toast.makeText(TeacherGradeInputActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        studentList.clear();
                        teacherStudentGradeAdapter.notifyDataSetChanged();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void saveGrades(boolean publish) {
        String selectedSubject = spinnerSubject.getSelectedItem().toString();
        if (selectedSubject.equals("Select Subject")) {
            Toast.makeText(this, "Please select a subject", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONArray gradesArray = new JSONArray();
        boolean allGradesFilled = true;

        for (Student student : teacherStudentGradeAdapter.getStudentList()) {
            String grade = student.getScore();
            Log.d("GradeDebug", "Student ID: " + student.getId() + ", Name: " + student.getName() + ", Retrieved Grade: '" + grade + "'");
            Log.d("GradeDebug", "Is Empty: " + grade.isEmpty() + ", Is Trimmed Empty: " + grade.trim().equals(""));

            if (grade.isEmpty() || grade.trim().equals("")) {
                allGradesFilled = false;
                Log.d("GradeDebug", "Found empty grade for student: " + student.getName());
                break;
            }
            try {
                JSONObject studentGradeJson = new JSONObject();
                studentGradeJson.put("student_id", student.getId());
                studentGradeJson.put("grade", grade);
                gradesArray.put(studentGradeJson);
            } catch (JSONException e) {
                Log.e("JSON Error", "Error creating JSON for student grade: " + e.getMessage());
                Toast.makeText(this, "Error preparing grade data", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!allGradesFilled) {
            Toast.makeText(this, "Please fill all grade fields, even with zero", Toast.LENGTH_LONG).show();
            return;
        }

        String url = BASE_URL + "teacher_save_grades.php";

        JSONObject postData = new JSONObject();
        try {
            postData.put("grades", gradesArray);
            postData.put("subject_name", selectedSubject);
            postData.put("publish", publish ? 1 : 0);
            postData.put("teacher_id", teacherId);
        } catch (JSONException e) {
            Log.e("JSON Error", "Error creating POST data: " + e.getMessage());
            Toast.makeText(this, "Error preparing data for sending", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(TeacherGradeInputActivity.this, 
                                    response.getString("message"), 
                                    Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(TeacherGradeInputActivity.this, 
                                    "Save failed: " + response.getString("message"), 
                                    Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e("Volley", "Error parsing save grades response: " + e.getMessage());
                            Toast.makeText(TeacherGradeInputActivity.this, 
                                "Error processing server response", 
                                Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error saving grades: " + error.getMessage());
                        Toast.makeText(TeacherGradeInputActivity.this, 
                            "Failed to connect to server: " + error.getMessage(), 
                            Toast.LENGTH_LONG).show();
                        if (error.networkResponse != null) {
                            Log.e("Volley", "Error Response code: " + error.networkResponse.statusCode);
                            Log.e("Volley", "Error Response data: " + new String(error.networkResponse.data));
                        }
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}