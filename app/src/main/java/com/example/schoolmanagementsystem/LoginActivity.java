package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    CheckBox checkboxRememberMe;
    Button buttonLogin, buttonRegister, buttonForgotPassword;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("rememberMe", false)) {
            editTextUsername.setText(sharedPreferences.getString("username", ""));
            editTextPassword.setText(sharedPreferences.getString("password", ""));
            checkboxRememberMe.setChecked(true);
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextUsername.getText().toString().trim().toLowerCase();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                NetworkUtils.loginStudent(LoginActivity.this, email, password, new NetworkUtils.NetworkCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            if ("success".equals(response.optString("status"))) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                if (checkboxRememberMe.isChecked()) {
                                    editor.putString("username", email);
                                    editor.putString("password", password);
                                    editor.putBoolean("rememberMe", true);
                                } else {
                                    editor.clear();
                                }
                                editor.apply();

                                JSONObject studentData = response.getJSONObject("student");

                                Intent intent = new Intent(LoginActivity.this, TeacherActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String message = response.optString("message", "Login failed");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(LoginActivity.this, "Login failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
