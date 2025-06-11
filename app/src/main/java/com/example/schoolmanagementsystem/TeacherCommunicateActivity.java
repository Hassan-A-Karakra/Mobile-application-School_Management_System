package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets; // Import for StandardCharsets

public class TeacherCommunicateActivity extends AppCompatActivity {
    private Spinner spinnerGrade;
    private Spinner spinnerSubject;
    private LinearLayout studentsContainer;
    private EditText editTextTitle;
    private EditText editTextMessage;
    private Button buttonSendMessage;
    private CheckBox checkBoxSelectAll;
    private List<CheckBox> studentCheckboxes;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_communicate);

        // Initialize views
        spinnerGrade = findViewById(R.id.spinnerGrade);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        studentsContainer = findViewById(R.id.studentsContainer);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);
        progressBar = findViewById(R.id.progressBar);
        checkBoxSelectAll = findViewById(R.id.checkBoxSelectAll);
        studentCheckboxes = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        // Load grades
        loadGrades();

        // Set up grade spinner listener
        spinnerGrade.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedGrade = parent.getItemAtPosition(position).toString();
                loadSubjects(selectedGrade);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Set up subject spinner listener
        spinnerSubject.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedGrade = spinnerGrade.getSelectedItem() != null ? 
                    spinnerGrade.getSelectedItem().toString() : "";
                if (!selectedGrade.isEmpty()) {
                    loadStudents(selectedGrade);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Set up select all checkbox listener
        checkBoxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (CheckBox checkBox : studentCheckboxes) {
                checkBox.setChecked(isChecked);
            }
        });

        // Set up send button
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        buttonSendMessage.setEnabled(!show);
    }

    private void loadGrades() {
        showLoading(true);
        String url = "http://10.0.2.2/student_system/teacher_get_grades.php";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    showLoading(false);
                    List<String> grades = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            grades.add(response.getString(i));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, grades);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerGrade.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TeacherCommunicateActivity.this, "Error parsing grades data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    showLoading(false);
                    Toast.makeText(TeacherCommunicateActivity.this, "Error loading grades: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(request);
    }

    private void loadSubjects(String grade) {
        showLoading(true);
        String url = "http://10.0.2.2/student_system/teacher_get_subjects.php?grade=" + grade;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    showLoading(false);
                    List<String> subjects = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            subjects.add(response.getString(i));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, subjects);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerSubject.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TeacherCommunicateActivity.this, "Error parsing subjects data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    showLoading(false);
                    Toast.makeText(TeacherCommunicateActivity.this, "Error loading subjects: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(request);
    }

    private void loadStudents(String grade) {
        String selectedSubject = spinnerSubject.getSelectedItem() != null ? 
            spinnerSubject.getSelectedItem().toString() : "";
            
        if (selectedSubject.isEmpty()) {
            return;
        }

        showLoading(true);
        String url = "http://10.0.2.2/student_system/teacher_communicate_with_get_students_by_class_and_subject.php?grade=" +
            grade + "&subject=" + selectedSubject;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    showLoading(false);
                    studentsContainer.removeAllViews();
                    studentCheckboxes.clear();
                    checkBoxSelectAll.setChecked(false);
                    try {
                        // Parse the full JSON object response
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getBoolean("success")) {
                            JSONArray studentsArray = jsonResponse.getJSONArray("students");
                            for (int i = 0; i < studentsArray.length(); i++) {
                                JSONObject student = studentsArray.getJSONObject(i);
                                LinearLayout studentRow = new LinearLayout(this);
                                studentRow.setOrientation(LinearLayout.HORIZONTAL);
                                studentRow.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));

                                CheckBox checkBox = new CheckBox(this);
                                checkBox.setText(student.getString("name"));
                                checkBox.setTag(student.getInt("id"));
                                studentCheckboxes.add(checkBox);
                                studentRow.addView(checkBox);
                                studentsContainer.addView(studentRow);
                            }
                        } else {
                            Toast.makeText(TeacherCommunicateActivity.this, 
                                "Error: " + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TeacherCommunicateActivity.this, "Error parsing students data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    showLoading(false);
                    Toast.makeText(TeacherCommunicateActivity.this, "Error loading students: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(request);
    }

    private void sendMessage() {
        String title = editTextTitle.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();
        List<Integer> selectedStudentIds = new ArrayList<>();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        for (CheckBox checkBox : studentCheckboxes) {
            if (checkBox.isChecked()) {
                selectedStudentIds.add((Integer) checkBox.getTag());
            }
        }

        if (selectedStudentIds.isEmpty()) {
            Toast.makeText(this, "Please select at least one student", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        String url = "http://10.0.2.2/student_system/teacher_send_message.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    showLoading(false);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getString("status").equals("success")) {
                            Toast.makeText(TeacherCommunicateActivity.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                            editTextTitle.setText("");
                            editTextMessage.setText("");
                            for (CheckBox checkBox : studentCheckboxes) {
                                checkBox.setChecked(false);
                            }
                        } else {
                            Toast.makeText(TeacherCommunicateActivity.this, "Error: " + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TeacherCommunicateActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    showLoading(false);
                    Toast.makeText(TeacherCommunicateActivity.this, "Error sending message: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("title", title);
                    jsonBody.put("message", message);
                    JSONArray jsonStudentIds = new JSONArray(selectedStudentIds);
                    jsonBody.put("student_ids", jsonStudentIds);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle error if JSON conversion fails
                }
                return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(request);
    }
}