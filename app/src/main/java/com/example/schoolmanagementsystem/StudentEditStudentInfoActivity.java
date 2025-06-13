package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class StudentEditStudentInfoActivity extends AppCompatActivity {

    private EditText editName, editPassword, editConfirmPassword;
    private Button btnSave;
    private static final String UPDATE_PROFILE_URL = "http://10.0.2.2/student_system/student_update_profile.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity_edit_student_info);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int studentId = prefs.getInt("student_id", -1);

        editName = findViewById(R.id.editStudentName);
        editPassword = findViewById(R.id.editStudentPassword);
        editConfirmPassword = findViewById(R.id.editStudentConfirmPassword);
        btnSave = findViewById(R.id.btnSaveChanges);

        // Set current name
        editName.setText(prefs.getString("student_name", ""));

        btnSave.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newPassword = editPassword.getText().toString().trim();
            String confirmPassword = editConfirmPassword.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.isEmpty()) {
                if (newPassword.length() < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            updateStudentProfile(studentId, newName, newPassword);
        });
    }

    private void updateStudentProfile(int studentId, String name, String password) {
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_PROFILE_URL,
                response -> {
                    if (response.contains("success")) {
                        // Update local storage
                        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("student_name", name);
                        editor.apply();

                        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", String.valueOf(studentId));
                params.put("name", name);
                if (!password.isEmpty()) {
                    params.put("password", password);
                }
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}