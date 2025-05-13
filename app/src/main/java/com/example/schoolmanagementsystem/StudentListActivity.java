package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewStudentList;
    private StudentAdapter studentAdapter;
    private List<String> studentList;
    private Button buttonShowStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        recyclerViewStudentList = findViewById(R.id.recyclerViewStudentList);
        buttonShowStudents = findViewById(R.id.buttonShowStudents);

        // إعداد RecyclerView
        recyclerViewStudentList.setLayoutManager(new LinearLayoutManager(this));

        // عند الضغط على زر "Show Students"
        buttonShowStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // جلب بيانات الطلاب هنا
                studentList = new ArrayList<>();
                studentList.add("Student 1");
                studentList.add("Student 2");
                studentList.add("Student 3");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");
                studentList.add("Student 4");


                // إعداد الـ Adapter لربط البيانات بـ RecyclerView
                studentAdapter = new StudentAdapter(studentList);
                recyclerViewStudentList.setAdapter(studentAdapter);

                Toast.makeText(StudentListActivity.this, "Displaying Student List", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
