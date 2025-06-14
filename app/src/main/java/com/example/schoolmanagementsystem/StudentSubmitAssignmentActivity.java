package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log; // Import Log for debugging
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.NetworkResponse; // Import NetworkResponse
import com.android.volley.VolleyError; // Import VolleyError
import org.json.JSONObject;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.content.Intent;

import java.nio.charset.StandardCharsets; // Import StandardCharsets

public class StudentSubmitAssignmentActivity extends AppCompatActivity {

    private EditText contentInput;
    private Button submitButton;
    private int assignmentId, studentId;
    private static final String SUBMIT_URL = "http://10.0.2.2/student_system/student_submit_assignment.php";
    private static final String TAG = "SubmitAssignment"; // Add TAG for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_submit_assignment);

        contentInput = findViewById(R.id.contentInput);
        submitButton = findViewById(R.id.submitButton);

        assignmentId = getIntent().getIntExtra("assignment_id", -1);
        studentId = getIntent().getIntExtra("student_id", -1);

        if (assignmentId == -1 || studentId == -1) {
            Toast.makeText(this, "Missing assignment or student info", Toast.LENGTH_SHORT).show();
            finish();
            Log.e(TAG, "Missing assignmentId (" + assignmentId + ") or studentId (" + studentId + ")");
            return;
        }

        submitButton.setOnClickListener(v -> submitAssignment());
    }

    private void submitAssignment() {
        String content = contentInput.getText().toString().trim();
        try {
            JSONObject data = new JSONObject();
            data.put("assignment_id", assignmentId);
            data.put("student_id", studentId);
            data.put("content", content); // Send the text content

            Log.d(TAG, "Submitting assignment. Data: " + data.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SUBMIT_URL, data,
                    response -> {
                        Log.d(TAG, "Submission successful. Response: " + response.toString());
                        
                        // Create success dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        View dialogView = getLayoutInflater().inflate(R.layout.dialog_success, null);
                        builder.setView(dialogView);
                        
                        AlertDialog dialog = builder.create();
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.show();
                        
                        // Set result to indicate successful submission
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("assignment_id", assignmentId);
                        resultIntent.putExtra("submitted", true);
                        setResult(RESULT_OK, resultIntent);
                        
                        // Auto dismiss after 2 seconds
                        dialog.getWindow().getDecorView().postDelayed(() -> {
                            dialog.dismiss();
                            finish();
                        }, 2000);
                    },
                    error -> {
                        // Detailed error logging
                        Log.e(TAG, "Submission error: " + error.getMessage());
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e(TAG, "Error status code: " + networkResponse.statusCode);
                            try {
                                String errorData = new String(networkResponse.data, StandardCharsets.UTF_8);
                                Log.e(TAG, "Error response data: " + errorData);
                                Toast.makeText(this, "Submission error: " + errorData, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e(TAG, "Error parsing network response data: " + e.getMessage());
                                Toast.makeText(this, "Submission error: Could not read server response", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "Submission error: " + (error.getMessage() != null ? error.getMessage() : "Unknown network error"), Toast.LENGTH_LONG).show();
                        }
                        error.printStackTrace(); // Print stack trace for full details
                    }
            );

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            Log.e(TAG, "Error preparing submission data: " + e.getMessage(), e);
            Toast.makeText(this, "Error preparing assignment submission", Toast.LENGTH_SHORT).show();
        }
    }
}