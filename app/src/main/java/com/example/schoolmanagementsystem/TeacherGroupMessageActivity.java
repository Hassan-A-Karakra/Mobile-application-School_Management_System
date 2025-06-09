package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherGroupMessageActivity extends AppCompatActivity {

    Spinner spinnerClasses;
    EditText editMessage;
    Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_group_message);

        spinnerClasses = findViewById(R.id.spinnerClasses);
        editMessage = findViewById(R.id.editMessage);
        buttonSend = findViewById(R.id.buttonSend);

        String[] classes = {"Class 1", "Class 2", "Class 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes);
        spinnerClasses.setAdapter(adapter);

        buttonSend.setOnClickListener(v -> {
            String selectedClass = spinnerClasses.getSelectedItem().toString();
            String message = editMessage.getText().toString().trim();

            if (message.isEmpty()) {
                Toast.makeText(TeacherGroupMessageActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                return;
            }

             Toast.makeText(TeacherGroupMessageActivity.this, "Message sent to " + selectedClass, Toast.LENGTH_SHORT).show();
            editMessage.setText("");
        });
    }
}
