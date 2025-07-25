package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button buttonRegistrar, buttonTeacher, buttonStudent;
//omar commit
    @Override
    protected void onCreate(Bundle savedInstanceState) {
/// Again commit test by hassan ahmad karakra
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRegistrar = findViewById(R.id.buttonRegistrar);
        buttonTeacher = findViewById(R.id.buttonTeacher);
        buttonStudent = findViewById(R.id.buttonStudent);

        // Registrar access
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterAdminLoginActivity.class);
                startActivity(intent);
            }
        });

        // Teacher login
        buttonTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TeacherLoginActivity.class);
                startActivity(intent);
            }
        });

         buttonStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StudentLoginActivity.class);
                startActivity(intent);
            }

        });
    }
}
