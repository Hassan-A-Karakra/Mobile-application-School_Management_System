package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AttendanceActivity extends AppCompatActivity {


    private RecyclerView attendanceRecyclerView;
    private AttendanceAdapter adapter;
    private Button buttonReports;
    private TextView textViewReport;
    private List<Student> studentList;

    private static final String API_URL = "http://10.0.2.2/student_system/get_students_attendance.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView);
        buttonReports = findViewById(R.id.buttonReports);
        textViewReport = findViewById(R.id.textViewReport);

        studentList = new ArrayList<>();
        adapter = new AttendanceAdapter(studentList);
        attendanceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceRecyclerView.setAdapter(adapter);

        fetchStudentsFromServer();

        buttonReports.setOnClickListener(v -> {
            StringBuilder report = new StringBuilder();
            report.append("Student Report:\n\n");
/*

            for (Student student : studentList) {
                report.append("Name: ").append(student.getName())
                        .append("\nAttendance: ").append(student.isPresent() ? "Present" : "Absent")
                        .append("\nGrade: ").append(student.getGrade())
                        .append("\n\n");
            }
*/
            textViewReport.setVisibility(View.VISIBLE);
            textViewReport.setText(report.toString());
            Toast.makeText(this, "Report Generated", Toast.LENGTH_SHORT).show();
        });
    }

    private void fetchStudentsFromServer() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                response -> {
                    studentList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String name = obj.getString("name");
                            boolean present = obj.optBoolean("present", false);
                            String grade = obj.optString("grade", "N/A");
                           // studentList.add(new Student(name, present, grade));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(request);
    }
}

