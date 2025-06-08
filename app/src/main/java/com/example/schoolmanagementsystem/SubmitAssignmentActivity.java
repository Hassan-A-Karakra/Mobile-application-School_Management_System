package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class SubmitAssignmentActivity extends AppCompatActivity {

    private EditText contentInput;
    private Button submitButton;
    private int assignmentId, studentId;
    private static final String SUBMIT_URL = "http://10.0.2.2/student_system/submit_assignment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_assignment);

        contentInput = findViewById(R.id.contentInput);
        submitButton = findViewById(R.id.submitButton);

        assignmentId = getIntent().getIntExtra("assignment_id", -1);
        studentId = getIntent().getIntExtra("student_id", -1);

        if (assignmentId == -1 || studentId == -1) {
            Toast.makeText(this, "Missing assignment or student info", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        submitButton.setOnClickListener(v -> submitAssignment());
    }

    private void submitAssignment() {
        try {
            JSONObject data = new JSONObject();
            data.put("assignment_id", assignmentId);
            data.put("student_id", studentId);
            data.put("content", contentInput.getText().toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SUBMIT_URL, data,
                    response -> {
                        Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show();
                        finish();
                    },
                    error -> Toast.makeText(this, "Submission error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(this).add(request);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error submitting assignment", Toast.LENGTH_SHORT).show();
        }
    }
}
