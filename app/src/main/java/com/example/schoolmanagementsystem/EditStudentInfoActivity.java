package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditStudentInfoActivity extends AppCompatActivity {

    private EditText editName, editClass;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity_edit_student_info);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int studentId = prefs.getInt("student_id", -1);

        editName = findViewById(R.id.editStudentName);
        editClass = findViewById(R.id.editStudentClass);
        btnSave = findViewById(R.id.btnSaveChanges);

        // Pre-fill fields
        editName.setText(prefs.getString("student_name", ""));
        editClass.setText(prefs.getString("student_class", ""));

        btnSave.setOnClickListener(v -> {
            String newName = editName.getText().toString();
            String newClass = editClass.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("student_name", newName);
            editor.putString("student_class", newClass);
            editor.apply();

            Toast.makeText(this, "Info updated (local only)", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
