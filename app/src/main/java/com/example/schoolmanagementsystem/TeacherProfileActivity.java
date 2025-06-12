package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TeacherProfileActivity extends AppCompatActivity {

    private static final String TAG = "TeacherProfileActivity";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String GET_PROFILE_URL = "http://10.0.2.2/student_system/teacher_get_profile.php";
    private static final String UPDATE_PROFILE_URL = "http://10.0.2.2/student_system/teacher_update_profile.php";

    ImageView profileImage;
    EditText editName, editEmail, editSubjectsTaught;
    Button buttonSave, buttonChangePhoto;

    Uri imageUri;
    private int teacherId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        teacherId = getIntent().getIntExtra("teacher_id", -1);
        Log.d(TAG, "Teacher ID from Intent: " + teacherId);

        if (teacherId == -1) {
            teacherId = sharedPreferences.getInt("current_teacher_id", -1);
            Log.d(TAG, "Teacher ID from SharedPreferences: " + teacherId);
        }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Teacher Profile");
        }

        profileImage = findViewById(R.id.profileImage);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editSubjectsTaught = findViewById(R.id.editSubjectsTaught);
        buttonSave = findViewById(R.id.buttonSave);
        buttonChangePhoto = findViewById(R.id.buttonChangePhoto);

        buttonChangePhoto.setOnClickListener(v -> openImageChooser());

        buttonSave.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String subjects = editSubjectsTaught.getText().toString().trim();

            updateTeacherProfile(teacherId, name, email, subjects);
        });

        if (teacherId != -1) {
            Log.d(TAG, "Final Teacher ID before fetching profile: " + teacherId);
            fetchTeacherProfile(teacherId);
        } else {
            Toast.makeText(this, "Teacher ID is required.", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Teacher ID is -1. Cannot fetch profile.");
            finish();
        }
    }

    private void fetchTeacherProfile(int id) {
        Log.d(TAG, "Attempting to fetch profile for teacher ID: " + id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PROFILE_URL,
                response -> {
                    try {
                        int jsonStartIndex = response.indexOf("{");
                        int jsonEndIndex = response.lastIndexOf("}");

                        String cleanResponse;
                        if (jsonStartIndex != -1 && jsonEndIndex != -1 && jsonEndIndex > jsonStartIndex) {
                            cleanResponse = response.substring(jsonStartIndex, jsonEndIndex + 1);
                        } else {
                            Log.e(TAG, "Invalid JSON response: No valid JSON object found. Response: " + response);
                            throw new JSONException("Invalid JSON response: No valid JSON object found.");
                        }

                        JSONObject jsonResponse = new JSONObject(cleanResponse);
                        String status = jsonResponse.getString("status");
                        if ("success".equals(status)) {
                            JSONObject teacher = jsonResponse.getJSONObject("teacher");
                            editName.setText(teacher.optString("name"));
                            editEmail.setText(teacher.optString("email"));
                            editSubjectsTaught.setText(teacher.optString("subject"));

                            Toast.makeText(TeacherProfileActivity.this, "Profile data loaded.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Profile data loaded successfully for teacher ID: " + id);
                        } else {
                            String message = jsonResponse.optString("message", "Failed to load profile.");
                            Toast.makeText(TeacherProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Failed to load profile for teacher ID: " + id + ". Message: " + message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TeacherProfileActivity.this, "Error parsing profile data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error parsing profile data: " + e.getMessage() + ". Response: " + response);
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(TeacherProfileActivity.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Network error fetching profile for teacher ID: " + id + ". Error: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("teacher_id", String.valueOf(id));
                Log.d(TAG, "GET_PROFILE_URL params: teacher_id=" + id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateTeacherProfile(int id, String name, String email, String subject) {
        Log.d(TAG, "Attempting to update profile for teacher ID: " + id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_PROFILE_URL,
                response -> {
                    try {
                        int jsonStartIndex = response.indexOf("{");
                        int jsonEndIndex = response.lastIndexOf("}");

                        String cleanResponse;
                        if (jsonStartIndex != -1 && jsonEndIndex != -1 && jsonEndIndex > jsonStartIndex) {
                            cleanResponse = response.substring(jsonStartIndex, jsonEndIndex + 1);
                        } else {
                            Log.e(TAG, "Invalid JSON response for update: No valid JSON object found. Response: " + response);
                            throw new JSONException("Invalid JSON response: No valid JSON object found.");
                        }

                        JSONObject jsonResponse = new JSONObject(cleanResponse);
                        String status = jsonResponse.getString("status");
                        String message = jsonResponse.optString("message");
                        if ("success".equals(status)) {
                            Toast.makeText(TeacherProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Profile updated successfully for teacher ID: " + id + ". Message: " + message);
                        } else {
                            Toast.makeText(TeacherProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Failed to update profile for teacher ID: " + id + ". Message: " + message);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TeacherProfileActivity.this, "Error parsing update response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Error parsing update response: " + e.getMessage() + ". Response: " + response);
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(TeacherProfileActivity.this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Network error updating profile for teacher ID: " + id + ". Error: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("teacher_id", String.valueOf(id));
                params.put("name", name);
                params.put("email", email);
                params.put("subject", subject);
                Log.d(TAG, "UPDATE_PROFILE_URL params: teacher_id=" + id + ", name=" + name + ", email=" + email + ", subject=" + subject);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}