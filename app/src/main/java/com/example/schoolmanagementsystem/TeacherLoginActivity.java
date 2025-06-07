package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TeacherLoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    CheckBox checkboxRememberMe;
    Button buttonLogin;

    SharedPreferences sharedPreferences;

    private static final String LOGIN_URL = "http://10.0.2.2/student_system/login_teacher.php";  // تأكد من أن الرابط صحيح

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);
        buttonLogin = findViewById(R.id.buttonLogin);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        // تحقق من بيانات الاعتماد المحفوظة
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            editTextUsername.setText(sharedPreferences.getString("username", ""));
            editTextPassword.setText(sharedPreferences.getString("password", ""));
            checkboxRememberMe.setChecked(true);
        }

        // التعامل مع الضغط على زر الدخول
        buttonLogin.setOnClickListener(v -> {
            String email = editTextUsername.getText().toString().trim().toLowerCase();
            String password = editTextPassword.getText().toString().trim();

            // تحقق من أن الحقول ليست فارغة
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(TeacherLoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // إرسال طلب تسجيل الدخول إلى الخادم
            loginTeacher(email, password);
        });
    }

    // دالة لإرسال طلب تسجيل الدخول إلى الخادم
    private void loginTeacher(String email, String password) {
        try {
            // تحضير البيانات لإرسالها
            JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("password", password);

            // إنشاء الطلب باستخدام Volley
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, params,
                    response -> {
                        // التعامل مع الاستجابة الناجحة
                        String status = response.optString("status");
                        if ("success".equals(status)) {
                            // حفظ بيانات الاعتماد إذا تم اختيار "تذكرني"
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if (checkboxRememberMe.isChecked()) {
                                editor.putString("username", email);
                                editor.putString("password", password);
                                editor.putBoolean("rememberMe", true);
                            } else {
                                editor.clear();
                            }
                            editor.apply();

                            try {
                                // استلام بيانات المعلم من الاستجابة
                                JSONObject teacher = response.getJSONObject("teacher");

                                // الانتقال إلى شاشة المعلم
                                Intent intent = new Intent(TeacherLoginActivity.this, TeacherActivity.class);
                                intent.putExtra("teacher_id", teacher.getInt("id"));
                                intent.putExtra("teacher_name", teacher.getString("name"));
                                intent.putExtra("teacher_email", teacher.getString("email"));
                                intent.putExtra("teacher_subject", teacher.getString("subject"));

                                Toast.makeText(TeacherLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                Toast.makeText(TeacherLoginActivity.this, "Error: Teacher data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // عرض رسالة الخطأ في حالة الفشل
                            Toast.makeText(TeacherLoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        // التعامل مع أخطاء الشبكة أو الخادم
                        Toast.makeText(TeacherLoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    });

            // إضافة الطلب إلى قائمة الطلبات في Volley
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        } catch (JSONException e) {
            // التعامل مع أخطاء إنشاء JSON
            Toast.makeText(TeacherLoginActivity.this, "Error creating JSON request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
