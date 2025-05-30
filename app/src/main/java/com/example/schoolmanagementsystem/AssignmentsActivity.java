package com.example.schoolmanagementsystem;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AssignmentsActivity extends AppCompatActivity {

    EditText assignmentEditText;
    Button submitAssignmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        // ربط الحقول
        assignmentEditText = findViewById(R.id.assignmentEditText);
        submitAssignmentButton = findViewById(R.id.submitAssignmentButton);

        // عند الضغط على زر "إضافة الواجب"
        submitAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String assignmentText = assignmentEditText.getText().toString();

                // تحقق من أن النص غير فارغ
                if (assignmentText.isEmpty()) {
                    Toast.makeText(AssignmentsActivity.this, "Please enter an assignment", Toast.LENGTH_SHORT).show();
                } else {
                    // هنا يمكن إضافة الكود لإرسال الواجب إلى قاعدة البيانات أو خدمة REST
                    Toast.makeText(AssignmentsActivity.this, "Assignment submitted successfully!", Toast.LENGTH_SHORT).show();
                    assignmentEditText.setText(""); // مسح النص بعد الإرسال
                }
            }
        });
    }
}
