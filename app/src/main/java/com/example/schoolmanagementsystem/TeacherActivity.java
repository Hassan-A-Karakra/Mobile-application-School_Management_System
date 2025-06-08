package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        buttonStudentList.setOnClickListener(v -> navigateToActivity(StudentListActivity.class));
        buttonGradeInput.setOnClickListener(v -> navigateToActivity(GradeInputActivity.class));
        buttonAttendance.setOnClickListener(v -> navigateToActivity(AttendanceActivity.class));
        buttonCommunicate.setOnClickListener(v -> navigateToActivity(CommunicateActivity.class));
        buttonSchedule.setOnClickListener(v -> navigateToActivity(ScheduleActivity.class));
        buttonAssignments.setOnClickListener(v -> navigateToActivity(AssignmentsActivity.class));
        buttonProfile.setOnClickListener(v -> navigateToActivity(ProfileActivity.class));
    }

    // دالة لتقليل التكرار وتحسين الكود
    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(TeacherActivity.this, activityClass);
        startActivity(intent);
    }

}
