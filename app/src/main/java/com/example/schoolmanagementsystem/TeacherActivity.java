package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherActivity extends AppCompatActivity {

    Button buttonStudentList, buttonGradeInput, buttonAttendance,
            buttonCommunicate, buttonSchedule, buttonAssignments, buttonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        buttonStudentList = findViewById(R.id.buttonStudentList);
        buttonGradeInput = findViewById(R.id.buttonGradeInput);
        buttonAttendance = findViewById(R.id.buttonAttendance);
        buttonCommunicate = findViewById(R.id.buttonCommunicate);
        buttonSchedule = findViewById(R.id.buttonSchedule);
        buttonAssignments = findViewById(R.id.buttonAssignments);
        buttonProfile = findViewById(R.id.buttonProfile);

        buttonStudentList.setOnClickListener(v -> navigateToActivity(RegisterStudentListActivity.class));
        buttonGradeInput.setOnClickListener(v -> navigateToActivity(TeacherGradeInputActivity.class));
        buttonAttendance.setOnClickListener(v -> navigateToActivity(TeacherAttendanceActivity.class));
        buttonCommunicate.setOnClickListener(v -> navigateToActivity(TeacherCommunicateActivity.class));
        buttonSchedule.setOnClickListener(v -> navigateToActivity(StudentScheduleActivity.class));
        buttonAssignments.setOnClickListener(v -> navigateToActivity(StudentAssignmentsActivity.class));
        buttonProfile.setOnClickListener(v -> navigateToActivity(TeacherProfileActivity.class));
    }

    // دالة لتقليل التكرار وتحسين الكود
    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(TeacherActivity.this, activityClass);
        startActivity(intent);
    }

}
