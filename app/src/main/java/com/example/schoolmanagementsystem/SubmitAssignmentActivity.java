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

    private EditText assignmentIdInput, studentIdInput, contentInput;
    private Button submitButton;
    private static final String SUBMIT_URL = "http://10.0.2.2/student_system/submit_assignment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_assignment);

        assignmentIdInput = findViewById(R.id.assignmentIdInput);
        studentIdInput = findViewById(R.id.studentIdInput);
        contentInput = findViewById(R.id.contentInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            try {
                JSONObject data = new JSONObject();
                data.put("assignment_id", Integer.parseInt(assignmentIdInput.getText().toString()));
                data.put("student_id", Integer.parseInt(studentIdInput.getText().toString()));
                data.put("content", contentInput.getText().toString());

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SUBMIT_URL, data,
                        response -> Toast.makeText(this, "Submitted!", Toast.LENGTH_SHORT).show(),
                        error -> Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                );

                Volley.newRequestQueue(this).add(request);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
