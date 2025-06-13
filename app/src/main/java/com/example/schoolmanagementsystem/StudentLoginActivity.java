package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log; // Added for logging
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import org.json.JSONException;

public class StudentLoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private static final String LOGIN_URL = "http://10.0.2.2/student_system/student_login_student.php";
    private static final String TAG = "StudentLogin"; // Added TAG for logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim().toLowerCase();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject requestData = new JSONObject();
            try {
                requestData.put("email", email);
                requestData.put("password", password);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, requestData,
                    response -> {
                        if ("success".equals(response.optString("status"))) {
                            try {
                                JSONObject student = response.getJSONObject("student");
                                int studentId = student.getInt("id"); // Get student ID from JSON
                                Log.d(TAG, "Student ID from JSON: " + studentId); // Log the student ID

                                // Save student info to SharedPreferences
                                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("student_id", studentId);
                                editor.putString("student_name", student.getString("name"));
                                editor.putString("student_email", student.getString("email"));
                                editor.putString("student_grade", student.getString("grade"));
                                editor.putString("student_class", student.getString("grade")); // Add this line to save class
                                editor.putInt("student_age", student.getInt("age"));
                                editor.apply();
                                Log.d(TAG, "Student ID saved to SharedPreferences: " + studentId); // Log after saving

                                // Launch dashboard
                                Intent intent = new Intent(this, StudentDashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                Log.e(TAG, "JSON parsing error: " + e.getMessage(), e); // Log JSON error
                                Toast.makeText(this, "Missing student data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String errorMessage = response.optString("message", "Invalid credentials");
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Login failed: " + errorMessage); // Log login failure
                        }
                    },
                    error -> {
                        Log.e(TAG, "Volley error: " + error.getMessage(), error); // Log Volley error
                        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);
        });
    }
}