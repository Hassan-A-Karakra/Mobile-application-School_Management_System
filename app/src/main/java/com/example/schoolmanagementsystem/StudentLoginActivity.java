
package com.example.schoolmanagementsystem;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private static final String LOGIN_URL = "http://10.0.2.2/student_system/login_student.php";

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

                                Intent intent = new Intent(this, StudentDashboardActivity.class);
                                intent.putExtra("student_id", student.getInt("id"));
                                intent.putExtra("student_name", student.getString("name"));
                                intent.putExtra("student_email", student.getString("email"));
                                intent.putExtra("student_grade", student.getString("grade"));
                                intent.putExtra("student_age", student.getInt("age"));

                                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                Toast.makeText(this, "Error: Student data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(this).add(request);
        });
    }
}
