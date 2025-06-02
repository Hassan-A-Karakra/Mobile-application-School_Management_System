package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GradeInputActivity extends AppCompatActivity {

    private EditText editTextStudentName, editTextGrade;
    private Button buttonSaveGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_input);

         editTextStudentName = findViewById(R.id.editTextStudentName);
        editTextGrade = findViewById(R.id.editTextGrade);
        buttonSaveGrade = findViewById(R.id.buttonSaveGrade);

         buttonSaveGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String studentName = editTextStudentName.getText().toString();
                String grade = editTextGrade.getText().toString();

                if (!studentName.isEmpty() && !grade.isEmpty()) {
                     Toast.makeText(GradeInputActivity.this, "Grade for " + studentName + ": " + grade, Toast.LENGTH_SHORT).show();

                   } else {
                     Toast.makeText(GradeInputActivity.this, "Please enter both student name and grade.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
