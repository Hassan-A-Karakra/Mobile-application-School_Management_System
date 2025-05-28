package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddSubjectActivity extends AppCompatActivity {

    EditText editTextSubjectName;
    Button buttonAddSubject;
    ListView listViewSubjects;

    ArrayList<String> subjectList;
    ArrayAdapter<String> adapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        editTextSubjectName = findViewById(R.id.editTextSubjectName);
        buttonAddSubject = findViewById(R.id.buttonAddSubject);
        listViewSubjects = findViewById(R.id.listViewSubjects);

        sharedPreferences = getSharedPreferences("SubjectsData", MODE_PRIVATE);
        subjectList = new ArrayList<>(sharedPreferences.getStringSet("subjects", new HashSet<>()));

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjectList);
        listViewSubjects.setAdapter(adapter);

        buttonAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subjectName = editTextSubjectName.getText().toString().trim();
                if (subjectName.isEmpty()) {
                    Toast.makeText(AddSubjectActivity.this, "Please enter subject name", Toast.LENGTH_SHORT).show();
                    return;
                }

                subjectList.add(subjectName);
                adapter.notifyDataSetChanged();

                // Save to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Set<String> set = new HashSet<>(subjectList);
                editor.putStringSet("subjects", set);
                editor.apply();

                editTextSubjectName.setText("");
                Toast.makeText(AddSubjectActivity.this, "Subject added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
