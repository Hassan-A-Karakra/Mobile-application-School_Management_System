package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
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
    private Button loginButton, registerButton;
    private CheckBox checkboxRememberMe;
    private static final String LOGIN_URL = "http://10.0.2.2/student_system/student_login_student.php";
    private static final String TAG = "StudentLogin";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("StudentPrefs", MODE_PRIVATE);

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.buttonRegister);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);

        // Check if remember me was checked previously
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            emailInput.setText(sharedPreferences.getString("email", ""));
            passwordInput.setText(sharedPreferences.getString("password", ""));
            checkboxRememberMe.setChecked(true);
        }

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
                                int studentId = student.getInt("id");
                                Log.d(TAG, "Student ID from JSON: " + studentId);

                                // Save student info to SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                // Handle Remember Me
                                if (checkboxRememberMe.isChecked()) {
                                    editor.putString("email", email);
                                    editor.putString("password", password);
                                    editor.putBoolean("rememberMe", true);
                                } else {
                                    editor.remove("email");
                                    editor.remove("password");
                                    editor.putBoolean("rememberMe", false);
                                }

                                // Save student data
                                editor.putInt("student_id", studentId);
                                editor.putString("student_name", student.getString("name"));
                                editor.putString("student_email", student.getString("email"));
                                editor.putString("student_grade", student.getString("grade"));
                                editor.putString("student_class", student.getString("grade"));
                                editor.putInt("student_age", student.getInt("age"));
                                editor.apply();

                                Log.d(TAG, "Student data saved to SharedPreferences");

                                // Launch dashboard
                                Intent intent = new Intent(this, StudentDashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                Log.e(TAG, "JSON parsing error: " + e.getMessage(), e);
                                Toast.makeText(this, "Missing student data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String errorMessage = response.optString("message", "Invalid credentials");
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Login failed: " + errorMessage);
                        }
                    },
                    error -> {
                        Log.e(TAG, "Volley error: " + error.getMessage(), error);
                        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterAdminLoginActivity.class);
            startActivity(intent);
        });
    }
} 