package com.example.schoolmanagementsystem;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TeacherAssignmentsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText assignmentTitleInput;
    private EditText assignmentDescriptionInput;
    private Spinner classSpinner;
    private Spinner subjectSpinner;
    private EditText dueDateInput;
    private Button createAssignmentButton;
    private RecyclerView assignmentsRecyclerView;
    private FloatingActionButton fabAddAssignment;
    private TeacherAssignmentAdapter assignmentAdapter;
    private View assignmentFormLayout;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat phpDateFormat;
    private String teacherGrade;

    private static final String BASE_URL = "http://10.0.2.2/student_system/";
    private static final String CREATE_ASSIGNMENT_URL = BASE_URL + "teacher_create_assignment.php";
    private static final String GET_ASSIGNMENTS_URL = BASE_URL + "teacher_get_teacher_assignments.php";
    private static final String GET_CLASSES_URL = BASE_URL + "teacher_get_grades.php";
    private static final String GET_SUBJECTS_URL = BASE_URL + "teacher_get_subjects.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_assignments);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        phpDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teacherGrade = extras.getString("TEACHER_GRADE");
            if (teacherGrade == null || teacherGrade.isEmpty()) {
                teacherGrade = "DefaultGrade";
                Toast.makeText(this, "No class data received, default value is used.", Toast.LENGTH_LONG).show();
            }
        } else {
            teacherGrade = "DefaultGrade";
            Toast.makeText(this, "No class data received, default value is used.", Toast.LENGTH_LONG).show();
        }

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupDatePicker();
        setupSpinners();
        setupClickListeners();

        assignmentFormLayout.setVisibility(View.VISIBLE);
        fabAddAssignment.setVisibility(View.GONE);

        fetchAssignments();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        assignmentTitleInput = findViewById(R.id.assignmentTitleInput);
        assignmentDescriptionInput = findViewById(R.id.assignmentDescriptionInput);
        classSpinner = findViewById(R.id.classSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        dueDateInput = findViewById(R.id.dueDateInput);
        createAssignmentButton = findViewById(R.id.createAssignmentButton);
        assignmentsRecyclerView = findViewById(R.id.assignmentsRecyclerView);
        fabAddAssignment = findViewById(R.id.fabAddAssignment);
        assignmentFormLayout = findViewById(R.id.createAssignmentCard);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Teacher Assignments");
        }
    }

    private void setupRecyclerView() {
        assignmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assignmentAdapter = new TeacherAssignmentAdapter(new TeacherAssignmentAdapter.OnAssignmentClickListener() {
            @Override
            public void onMenuClick(Assignment assignment) {
                Toast.makeText(TeacherAssignmentsActivity.this, "Menu clicked for: " + assignment.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        assignmentsRecyclerView.setAdapter(assignmentAdapter);
    }

    private void setupDatePicker() {
        dueDateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        dueDateInput.setFocusable(false);
        dueDateInput.setKeyListener(null);
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    dueDateInput.setText(dateFormatter.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupSpinners() {
        fetchClasses();
        fetchSubjects();
    }

    private void fetchClasses() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(GET_CLASSES_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    os.write(new JSONObject().toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        StringBuilder response = new StringBuilder();
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        String jsonResponse = response.toString();
                        Log.d("API_RESPONSE", "Classes Response: " + jsonResponse);

                        JSONArray classesJson = new JSONArray(jsonResponse);
                        final List<String> fetchedClasses = new ArrayList<>();
                        fetchedClasses.add("Select Class");
                        for (int i = 0; i < classesJson.length(); i++) {
                            fetchedClasses.add(classesJson.getString(i));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> classAdapter = new ArrayAdapter<>(TeacherAssignmentsActivity.this,
                                        android.R.layout.simple_spinner_item, fetchedClasses);
                                classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                classSpinner.setAdapter(classAdapter);
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("API_ERROR", "Error fetching classes", e);
                    final String errorMessage = "Network Error: " + e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TeacherAssignmentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    if (conn != null) conn.disconnect();
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void fetchSubjects() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(GET_SUBJECTS_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    os.write(new JSONObject().toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        StringBuilder response = new StringBuilder();
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        String jsonResponse = response.toString();
                        Log.d("API_RESPONSE", "Subjects Response: " + jsonResponse);

                        JSONArray subjectsJson = new JSONArray(jsonResponse);
                        final List<String> fetchedSubjects = new ArrayList<>();
                        fetchedSubjects.add("Select Subject");
                        for (int i = 0; i < subjectsJson.length(); i++) {
                            fetchedSubjects.add(subjectsJson.getString(i));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(TeacherAssignmentsActivity.this,
                                        android.R.layout.simple_spinner_item, fetchedSubjects);
                                subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                subjectSpinner.setAdapter(subjectAdapter);
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("API_ERROR", "Error fetching subjects", e);
                    final String errorMessage = "Network Error: " + e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TeacherAssignmentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    if (conn != null) conn.disconnect();
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void setupClickListeners() {
        fabAddAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignmentFormLayout.setVisibility(View.VISIBLE);
                fabAddAssignment.setVisibility(View.GONE);
                clearInputFields();
            }
        });

        createAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAssignment();
            }
        });
    }

    private void createAssignment() {
        String title = assignmentTitleInput.getText().toString().trim();
        String description = assignmentDescriptionInput.getText().toString().trim();
        String assignmentClass = classSpinner.getSelectedItem().toString();
        String assignmentSubject = subjectSpinner.getSelectedItem().toString();
        String dueDate = dueDateInput.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty() ||
                assignmentClass.equals("Select Class") || assignmentSubject.equals("Select Subject")) {
            Toast.makeText(this, "Please fill all fields and select Class/Subject.", Toast.LENGTH_SHORT).show();
            return;
        }

        String dueDateForPhp;
        try {
            dueDateForPhp = phpDateFormat.format(dateFormatter.parse(dueDate));
        } catch (java.text.ParseException e) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("title", title);
            postData.put("description", description);
            postData.put("due_date", dueDateForPhp);
            postData.put("class", assignmentClass);
            postData.put("subject", assignmentSubject);
            postData.put("grade", teacherGrade);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(CREATE_ASSIGNMENT_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    os.write(postData.toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        String jsonResponse = response.toString();
                        Log.d("API_RESPONSE", "Create Assignment Response: " + jsonResponse);

                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        final String status = jsonObject.getString("status");
                        final String message = jsonObject.optString("message", "An unknown error occurred.");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if ("success".equals(status)) {
                                    Toast.makeText(TeacherAssignmentsActivity.this, "Assignment created successfully!", Toast.LENGTH_SHORT).show();
                                    clearInputFields();
                                    assignmentFormLayout.setVisibility(View.GONE);
                                    fabAddAssignment.setVisibility(View.VISIBLE);
                                    fetchAssignments();
                                } else {
                                    Toast.makeText(TeacherAssignmentsActivity.this, "Failed to create assignment: " + message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("API_ERROR", "Error creating assignment", e);
                    final String errorMessage = "Network Error: " + e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TeacherAssignmentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    if (conn != null) conn.disconnect();
                }
            }
        }).start();
    }

    private void fetchAssignments() {
        if (teacherGrade == null || teacherGrade.isEmpty()) {
            Toast.makeText(this, "Failed to load assignments: Grade is required for fetching assignments.", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("grade", teacherGrade);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(GET_ASSIGNMENTS_URL);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    os.write(postData.toString().getBytes("UTF-8"));
                    os.flush();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        StringBuilder response = new StringBuilder();
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        String jsonResponse = response.toString();
                        Log.d("API_RESPONSE", "Assignments Response: " + jsonResponse);

                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        String status = jsonObject.getString("status");

                        if ("success".equals(status)) {
                            JSONArray assignmentsJson = jsonObject.getJSONArray("assignments");
                            final List<Assignment> fetchedAssignments = new ArrayList<>();
                            for (int i = 0; i < assignmentsJson.length(); i++) {
                                JSONObject assignmentJson = assignmentsJson.getJSONObject(i);
                                int id = assignmentJson.getInt("id");
                                String title = assignmentJson.getString("title");
                                String description = assignmentJson.getString("description");
                                String dueDate = assignmentJson.getString("due_date");
                                String assignmentClass = assignmentJson.getString("class");
                                String assignmentSubject = assignmentJson.getString("subject");

                                fetchedAssignments.add(new Assignment(id, title, description, dueDate, assignmentClass, assignmentSubject));
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (assignmentAdapter != null) {
                                        assignmentAdapter.submitList(fetchedAssignments);
                                        if (fetchedAssignments.isEmpty()) {
                                            Toast.makeText(TeacherAssignmentsActivity.this, "No assignments to display.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    Log.e("API_ERROR", "Error fetching assignments", e);
                    final String errorMessage = "Network Error: " + e.getMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TeacherAssignmentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    if (conn != null) conn.disconnect();
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    private void clearInputFields() {
        assignmentTitleInput.setText("");
        assignmentDescriptionInput.setText("");
        classSpinner.setSelection(0);
        subjectSpinner.setSelection(0);
        dueDateInput.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}