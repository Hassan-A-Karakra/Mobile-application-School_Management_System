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

    public class LoginActivity extends AppCompatActivity {

        EditText editTextUsername, editTextPassword;
        CheckBox checkboxRememberMe;
        Button buttonLogin;

        SharedPreferences sharedPreferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            editTextUsername = findViewById(R.id.editTextUsername);
            editTextPassword = findViewById(R.id.editTextPassword);
            checkboxRememberMe = findViewById(R.id.checkboxRememberMe);
            buttonLogin = findViewById(R.id.buttonLogin);

            sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

            // Retrieve saved login data if "Remember Me" was checked
            if (sharedPreferences.getBoolean("rememberMe", false)) {
                editTextUsername.setText(sharedPreferences.getString("username", ""));
                editTextPassword.setText(sharedPreferences.getString("password", ""));
                checkboxRememberMe.setChecked(true);
            }

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = editTextUsername.getText().toString();
                    String password = editTextPassword.getText().toString();

                    if (username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Save login data if "Remember Me" is checked
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (checkboxRememberMe.isChecked()) {
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putBoolean("rememberMe", true);
                    } else {
                        editor.clear(); // Clear saved data if "Remember Me" is unchecked
                    }
                    editor.apply();

                    // Navigate to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }