package com.example.schoolmanagementsystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64; // Import for Base64 encoding
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView; // Import for TextView
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher; // Import for ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts; // Import for ActivityResultContracts
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
import java.io.ByteArrayOutputStream; // Import for ByteArrayOutputStream
import java.io.InputStream; // Import for InputStream
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
    private Spinner gradeSpinner;
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

    // New variables for file selection
    private Button buttonChooseFile;
    private TextView textViewFileName;
    private Uri selectedFileUri = null;
    private String selectedFileName = "";
    private String selectedFileBase64 = "";
    private ActivityResultLauncher<Intent> filePickerLauncher;

    private static final String BASE_URL = "http://10.0.2.2/student_system/";
    private static final String CREATE_ASSIGNMENT_URL = BASE_URL + "teacher_create_assignment.php";
    private static final String GET_ASSIGNMENTS_URL = BASE_URL + "teacher_get_teacher_assignments.php";
    private static final String GET_GRADES_URL = BASE_URL + "teacher_get_grades.php";
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

        // Initialize file picker launcher
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedFileUri = result.getData().getData();
                        if (selectedFileUri != null) {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                                int bufferSize = 1024;
                                byte[] buffer = new byte[bufferSize];
                                int len;
                                while ((len = inputStream.read(buffer)) != -1) {
                                    byteBuffer.write(buffer, 0, len);
                                }
                                selectedFileBase64 = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);

                                // Get file name from URI
                                String path = selectedFileUri.getPath();
                                selectedFileName = path.substring(path.lastIndexOf("/") + 1); // Get filename from path

                                textViewFileName.setText(selectedFileName);
                                Toast.makeText(this, "File selected: " + selectedFileName, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.e("FilePicker", "Error reading file: " + e.getMessage());
                                Toast.makeText(this, "Error reading file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                selectedFileUri = null;
                                selectedFileName = "";
                                selectedFileBase64 = "";
                                textViewFileName.setText("No file selected");
                            }
                        }
                    }
                }
        );
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        assignmentTitleInput = findViewById(R.id.assignmentTitleInput);
        assignmentDescriptionInput = findViewById(R.id.assignmentDescriptionInput);
        gradeSpinner = findViewById(R.id.classSpinner);
        subjectSpinner = findViewById(R.id.subjectSpinner);
        dueDateInput = findViewById(R.id.dueDateInput);
        createAssignmentButton = findViewById(R.id.createAssignmentButton);
        assignmentsRecyclerView = findViewById(R.id.assignmentsRecyclerView);
        fabAddAssignment = findViewById(R.id.fabAddAssignment);
        assignmentFormLayout = findViewById(R.id.createAssignmentCard);

        // Initialize new views for file selection
        buttonChooseFile = findViewById(R.id.buttonChooseFile);
        textViewFileName = findViewById(R.id.textViewFileName);
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
        fetchGrades();
        fetchSubjects();
    }

    private void fetchGrades() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(GET_GRADES_URL);
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
                        Log.d("API_RESPONSE", "Grades Response: " + jsonResponse);

                        JSONArray gradesJson = new JSONArray(jsonResponse);
                        final List<String> fetchedGrades = new ArrayList<>();
                        fetchedGrades.add("Select Grade");
                        for (int i = 0; i < gradesJson.length(); i++) {
                            fetchedGrades.add(gradesJson.getString(i));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayAdapter<String> gradeAdapter = new ArrayAdapter<>(TeacherAssignmentsActivity.this,
                                        android.R.layout.simple_spinner_item, fetchedGrades);
                                gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                gradeSpinner.setAdapter(gradeAdapter);
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("API_ERROR", "Error fetching grades", e);
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

        // Set up click listener for choose file button
        buttonChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Select a File"));
    }

    private void createAssignment() {
        String title = assignmentTitleInput.getText().toString().trim();
        String description = assignmentDescriptionInput.getText().toString().trim();
        String assignmentGrade = gradeSpinner.getSelectedItem().toString();
        String assignmentSubject = subjectSpinner.getSelectedItem().toString();
        String dueDate = dueDateInput.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || dueDate.isEmpty() ||
                assignmentGrade.equals("Select Grade") || assignmentSubject.equals("Select Subject")) {
            Toast.makeText(this, "Please fill all fields and select Grade/Subject.", Toast.LENGTH_SHORT).show();
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
            postData.put("subject", assignmentSubject);
            postData.put("grade", assignmentGrade);
            // Add file data if selected
            if (selectedFileUri != null && !selectedFileBase64.isEmpty()) {
                postData.put("file_name", selectedFileName);
                postData.put("file_content", selectedFileBase64);
            }
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
                                String assignmentSubject = assignmentJson.getString("subject");
                                String assignmentGrade = assignmentJson.getString("grade");
                                // Retrieve file_name and file_path (if available)
                                String fileName = assignmentJson.optString("file_name", null);
                                String filePath = assignmentJson.optString("file_path", null);


                                // Assuming Assignment constructor can handle file_name and file_path
                                fetchedAssignments.add(new Assignment(id, title, description, dueDate, assignmentSubject, assignmentGrade, fileName, filePath));
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
        gradeSpinner.setSelection(0);
        subjectSpinner.setSelection(0);
        dueDateInput.setText("");
        // Clear file selection
        selectedFileUri = null;
        selectedFileName = "";
        selectedFileBase64 = "";
        textViewFileName.setText("No file selected");
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