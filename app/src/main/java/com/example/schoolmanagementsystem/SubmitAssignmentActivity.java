package com.example.schoolmanagementsystem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class SubmitAssignmentActivity extends AppCompatActivity {

    private static final String TAG = "SubmitAssignmentAct";
    private static final String SUBMIT_URL = "http://10.0.2.2/student_system/submit_assignment.php";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int MAX_FILE_SIZE_KB = 500;

    private EditText contentInput;
    private Button submitButton, attachFileButton;
    private TextView assignmentInfoTextView, fileNameTextView;

    private int assignmentId, studentId;
    private Uri selectedFileUri;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedFileUri = result.getData().getData();
                    if (selectedFileUri != null) {
                        String fileName = getFileName(selectedFileUri);
                        fileNameTextView.setText(fileName);
                        Log.d(TAG, "Selected file: " + fileName);
                    } else {
                        fileNameTextView.setText("No file selected");
                    }
                } else {
                    fileNameTextView.setText("No file selected");
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_assignment);

        initializeViews();
        setupClickListeners();
        loadAssignmentData();
    }

    private void initializeViews() {
        contentInput = findViewById(R.id.contentInput);
        submitButton = findViewById(R.id.submitButton);
        attachFileButton = findViewById(R.id.attachFileButton);
        assignmentInfoTextView = findViewById(R.id.assignmentInfoTextView);
        fileNameTextView = findViewById(R.id.fileNameTextView);
    }

    private void setupClickListeners() {
        submitButton.setOnClickListener(v -> submitAssignment());
        attachFileButton.setOnClickListener(v -> checkPermissionAndPickFile());
    }

    private void loadAssignmentData() {
        assignmentId = getIntent().getIntExtra("assignment_id", -1);
        studentId = getIntent().getIntExtra("student_id", -1);
        String assignmentTitle = getIntent().getStringExtra("assignment_title");

        if (assignmentId == -1 || studentId == -1) {
            Toast.makeText(this, "Missing assignment or student info.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (assignmentTitle != null && !assignmentTitle.isEmpty()) {
            assignmentInfoTextView.setText("Submitting for: " + assignmentTitle + " | Your ID: " + studentId);
        } else {
            assignmentInfoTextView.setText("Assignment ID: " + assignmentId + " | Your ID: " + studentId);
        }
    }

    private void checkPermissionAndPickFile() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(Intent.createChooser(intent, "Select a file"));
    }

    private String getFileName(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "getFileName: " + e.getMessage(), e);
            }
        }
        if (result == null) {
            result = uri.getPath();
            if (result != null) {
                int cut = result.lastIndexOf(File.separator);
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }
        return result != null ? result : "Unknown File";
    }

    private String getFileContentAsBase64(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalBytes = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
                if (totalBytes > MAX_FILE_SIZE_KB * 1024) {
                    Toast.makeText(this, "File too large. Max: " + MAX_FILE_SIZE_KB + "KB", Toast.LENGTH_LONG).show();
                    return null;
                }
            }

            return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e(TAG, "Base64 conversion failed: " + e.getMessage(), e);
            Toast.makeText(this, "Error reading file.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void submitAssignment() {
        String content = contentInput.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Please enter assignment content.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject data = new JSONObject();
            data.put("assignment_id", assignmentId);
            data.put("student_id", studentId);
            data.put("content", content);

            if (selectedFileUri != null) {
                String base64FileContent = getFileContentAsBase64(selectedFileUri);
                if (base64FileContent != null) {
                    data.put("has_attachment", true);
                    data.put("file_name", getFileName(selectedFileUri));
                    data.put("file_content", base64FileContent);
                } else {
                    data.put("has_attachment", false);
                }
            } else {
                data.put("has_attachment", false);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SUBMIT_URL, data,
                    response -> {
                        String status = response.optString("status", "error");
                        String message = response.optString("message", "Unknown response from server.");
                        if ("success".equals(status)) {
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Submission failed: " + message, Toast.LENGTH_LONG).show();
                        }
                    },
                    error -> {
                        String errorMessage = "Error during submission: " +
                                (error.getMessage() != null ? error.getMessage() : "No message.");
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                    }
            );

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            Log.e(TAG, "Submission JSON error: " + e.getMessage(), e);
            Toast.makeText(this, "Error preparing submission.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        } else {
            Toast.makeText(this, "Permission denied. Enable file access in settings.", Toast.LENGTH_LONG).show();
        }
    }
}
