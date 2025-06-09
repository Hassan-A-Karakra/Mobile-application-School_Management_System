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
    private Spinner classSpinner; // Spinner for Class selection
    private Spinner subjectSpinner; // Spinner for Subject selection
    private EditText dueDateInput;
    private Button createAssignmentButton;
    private RecyclerView assignmentsRecyclerView;
    private FloatingActionButton fabAddAssignment;
    private TeacherAssignmentAdapter assignmentAdapter;
    private View assignmentFormLayout; // MaterialCardView for the form
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat phpDateFormat; // Date format for PHP
    private String teacherGrade; // The class/grade the teacher teaches

    // URLs for PHP backend files
    // IMPORTANT: Make sure to replace 192.168.1.103 with the actual IP address of your XAMPP server
    private static final String BASE_URL = "http://192.168.1.103/student_system/"; // or http://10.0.2.2/student_system/ for emulator
    private static final String CREATE_ASSIGNMENT_URL = BASE_URL + "create_assignment.php";
    private static final String GET_ASSIGNMENTS_URL = BASE_URL + "get_teacher_assignments.php";
    private static final String GET_CLASSES_URL = BASE_URL + "teacher_get_classes.php"; // New URL for fetching classes
    private static final String GET_SUBJECTS_URL = BASE_URL + "teacher_get_subjects.php"; // New URL for fetching subjects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_assignments);

        // Initialize variables
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        phpDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // PHP date format

        // IMPORTANT: Receive the teacher's grade from the Intent
        // This value should be passed from the TeacherLoginActivity upon successful login
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teacherGrade = extras.getString("TEACHER_GRADE");
            if (teacherGrade == null || teacherGrade.isEmpty()) {
                // Fallback if grade is not provided or empty (should ideally not happen if login passes it)
                teacherGrade = "DefaultGrade"; // Set a default value if not received
                Toast.makeText(this, "No class data received, default value is used.", Toast.LENGTH_LONG).show();
            }
        } else {
            teacherGrade = "DefaultGrade"; // Default value if no extras are passed at all
            Toast.makeText(this, "No class data received, default value is used.", Toast.LENGTH_LONG).show();
        }

        // Bind UI elements
        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupDatePicker();
        setupSpinners(); // Call this to set up Spinners dynamically
        setupClickListeners();

        // Hide assignment creation form initially
        assignmentFormLayout.setVisibility(View.GONE);

        // Fetch assignments when the activity starts
        fetchAssignments();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        assignmentTitleInput = findViewById(R.id.assignmentTitleInput);
        assignmentDescriptionInput = findViewById(R.id.assignmentDescriptionInput);
        classSpinner = findViewById(R.id.classSpinner); // Bind class Spinner
        subjectSpinner = findViewById(R.id.subjectSpinner); // Bind subject Spinner
        dueDateInput = findViewById(R.id.dueDateInput);
        createAssignmentButton = findViewById(R.id.createAssignmentButton);
        assignmentsRecyclerView = findViewById(R.id.assignmentsRecyclerView);
        fabAddAssignment = findViewById(R.id.fabAddAssignment);
        assignmentFormLayout = findViewById(R.id.createAssignmentCard); // Bind the CardView
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
                // Here you can add logic to handle the assignment menu (e.g., edit/delete)
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
        dueDateInput.setFocusable(false); // Prevent keyboard from popping up
        dueDateInput.setKeyListener(null); // Prevent manual input
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
        fetchClasses(); // Fetch classes from backend to populate the spinner
        fetchSubjects(); // Fetch subjects from backend to populate the spinner
    }

    private void fetchClasses() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(GET_CLASSES_URL);
                    conn = (HttpURLConnection) url.openConnection(); // Corrected line!
                    conn.setRequestMethod("GET");

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

                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        String status = jsonObject.getString("status");

                        if ("success".equals(status)) {
                            JSONArray classesJson = jsonObject.getJSONArray("classes");
                            final List<String> fetchedClasses = new ArrayList<>();
                            fetchedClasses.add("Select Class"); // Add default hint
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
                        } else {
                            final String message = jsonObject.optString("message", "Failed to fetch classes.");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TeacherAssignmentsActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        final String errorMessage = "Error fetching classes: " + responseCode;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TeacherAssignmentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
                    if (conn != null) {
                        conn.disconnect();
                    }
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
                    conn = (HttpURLConnection) url.openConnection(); // Corrected line!
                    conn.setRequestMethod("GET");

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

                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        String status = jsonObject.getString("status");

                        if ("success".equals(status)) {
                            JSONArray subjectsJson = jsonObject.getJSONArray("subjects");
                            final List<String> fetchedSubjects = new ArrayList<>();
                            fetchedSubjects.add("Select Subject"); // Add default hint
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
                        } else {
                            final String message = jsonObject.optString("message", "Failed to fetch subjects.");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TeacherAssignmentsActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        final String errorMessage = "Error fetching subjects: " + responseCode;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TeacherAssignmentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
                    if (conn != null) {
                        conn.disconnect();
                    }
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
                // Show the assignment creation form
                assignmentFormLayout.setVisibility(View.VISIBLE);
                fabAddAssignment.setVisibility(View.GONE); // Hide the add button
                clearInputFields(); // Clear fields when opening the form
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
        String assignmentClass = classSpinner.getSelectedItem().toString(); // Get selected class from Spinner
        String assignmentSubject = subjectSpinner.getSelectedItem().toString(); // Get selected subject from Spinner
        String dueDate = dueDateInput.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty() ||
                assignmentClass.equals("Select Class") || assignmentSubject.equals("Select Subject")) {
            Toast.makeText(this, "Please fill all fields and select Class/Subject.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert date from UI format (dd/MM/yyyy) to database format (yyyy-MM-dd)
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
            postData.put("class", assignmentClass); // Add class to POST data
            postData.put("subject", assignmentSubject); // Add subject to POST data
            postData.put("grade", teacherGrade); // Send teacher's grade with assignment (to link to teacher/students)
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
                                    assignmentFormLayout.setVisibility(View.GONE); // Hide the form
                                    fabAddAssignment.setVisibility(View.VISIBLE); // Show the add button
                                    fetchAssignments(); // Re-fetch assignments after creation
                                } else {
                                    Toast.makeText(TeacherAssignmentsActivity.this, "Failed to create assignment: " + message, Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        final String errorMessage = "Error: " + responseCode;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TeacherAssignmentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
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
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    // Function to fetch assignments from the backend
    private void fetchAssignments() {
        // If teacherGrade is not available, cannot fetch assignments
        if (teacherGrade == null || teacherGrade.isEmpty()) {
            Toast.makeText(this, "Failed to load assignments: Grade is required for fetching assignments.", Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("grade", teacherGrade);
            // If you want to fetch assignments for a specific class and subject, you can add it here
            // postData.put("class", "some_class");
            // postData.put("subject", "some_subject");
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
                                String assignmentClass = assignmentJson.getString("class"); // Get class value
                                String assignmentSubject = assignmentJson.getString("subject"); // Get subject value

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
                        } else {
                            final String message = jsonObject.optString("message", "Failed to fetch assignments.");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TeacherAssignmentsActivity.this, message, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        final String errorMessage = "Error fetching assignments: " + responseCode;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TeacherAssignmentsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        });
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
                    if (conn != null) {
                        conn.disconnect();
                    }
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
        classSpinner.setSelection(0); // Reset class Spinner to default
        subjectSpinner.setSelection(0); // Reset subject Spinner to default
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