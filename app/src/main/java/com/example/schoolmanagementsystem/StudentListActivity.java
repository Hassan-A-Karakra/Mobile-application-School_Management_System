package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private ArrayList<Student> studentList = new ArrayList<>();
    private static final String URL = "http://10.0.2.2/student_system/view_students.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        recyclerView = findViewById(R.id.recyclerViewStudentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this, studentList);
        recyclerView.setAdapter(adapter);

        Button showStudentsBtn = findViewById(R.id.buttonShowStudents);
        showStudentsBtn.setOnClickListener(v -> loadStudentData());
    }

    private void loadStudentData() {
        com.android.volley.toolbox.JsonObjectRequest request = new com.android.volley.toolbox.JsonObjectRequest(
                Request.Method.GET, URL, null,
                response -> {
                    try {
                        studentList.clear();
                        if (response.getString("status").equals("success")) {
                            org.json.JSONArray studentsArray = response.getJSONArray("students");
                            for (int i = 0; i < studentsArray.length(); i++) {
                                JSONObject obj = studentsArray.getJSONObject(i);

                               /* Student student = new Student(
                                        obj.getInt("id"),
                                        obj.getString("name"),
                                        obj.getString("email"),
                                   //     obj.getInt("age")
                                );


                                studentList.add(student);

                                */
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "No students found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Failed to fetch students", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }

}
