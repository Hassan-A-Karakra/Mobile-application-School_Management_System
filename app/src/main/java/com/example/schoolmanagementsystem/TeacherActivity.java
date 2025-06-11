package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Import for MenuItem
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class TeacherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private int currentTeacherId; // متغير لتخزين teacher_id

    // Original Button declarations - NOW RESTORED
    Button buttonStudentList, buttonGradeInput, buttonAttendance,
            buttonCommunicate, buttonSchedule, buttonAssignments, buttonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        // جلب teacher_id الذي تم تمريره من TeacherLoginActivity
        currentTeacherId = getIntent().getIntExtra("teacher_id", -1);
        if (currentTeacherId == -1) {
            // يمكنك إضافة رسالة Toast هنا إذا لم يتم جلب الـ ID
            // Toast.makeText(this, "خطأ: لم يتم جلب معرف المعلم.", Toast.LENGTH_SHORT).show();
            // يمكنك أيضًا إغلاق النشاط أو إعادة التوجيه لشاشة تسجيل الدخول
        }

        // --- NEW: Navigation Drawer Setup ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // These lines are now fully functional alongside the navigation drawer.
        buttonStudentList = findViewById(R.id.buttonStudentList);
        buttonGradeInput = findViewById(R.id.buttonGradeInput);
        buttonAttendance = findViewById(R.id.buttonAttendance);
        buttonCommunicate = findViewById(R.id.buttonCommunicate);
        buttonSchedule = findViewById(R.id.buttonSchedule);
        buttonAssignments = findViewById(R.id.buttonAssignments);
        // buttonProfile = findViewById(R.id.buttonProfile); // This line is still commented out if it was before, assuming it's not implemented yet.

        buttonStudentList.setOnClickListener(v -> navigateToActivity(TeacherStudentListActivity.class, currentTeacherId));
        buttonGradeInput.setOnClickListener(v -> navigateToActivity(TeacherGradeInputActivity.class, currentTeacherId));
        buttonAttendance.setOnClickListener(v -> navigateToActivity(TeacherAttendanceActivity.class, currentTeacherId));
        buttonCommunicate.setOnClickListener(v -> navigateToActivity(TeacherCommunicateActivity.class, currentTeacherId));
        // تم تصحيح هذا السطر: يفتح TeacherScheduleActivity ويمرر teacher_id
        buttonSchedule.setOnClickListener(v -> navigateToActivity(TeacherScheduleActivity.class, currentTeacherId));
        buttonAssignments.setOnClickListener(v -> navigateToActivity(TeacherAssignmentsActivity.class, currentTeacherId));
        //buttonProfile.setOnClickListener(v -> navigateToActivity(TeacherProfileActivity.class, currentTeacherId)); // This line is still commented out if it was before, assuming it's not implemented yet.
        // --- END RESTORED: Original Button Initializations and Listeners ---
    }

    // This method is called when an item in the navigation drawer is selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_student_list) {
            navigateToActivity(RegisterStudentListActivity.class, currentTeacherId);
        } else if (id == R.id.nav_grade_input) {
            navigateToActivity(TeacherGradeInputActivity.class, currentTeacherId);
        } else if (id == R.id.nav_attendance) {
            navigateToActivity(TeacherAttendanceActivity.class, currentTeacherId);
        } else if (id == R.id.nav_communicate) {
            navigateToActivity(TeacherCommunicateActivity.class, currentTeacherId);
            // تم تصحيح هذا السطر: يفتح TeacherScheduleActivity ويمرر teacher_id
        } else if (id == R.id.nav_schedule) {
            navigateToActivity(TeacherScheduleActivity.class, currentTeacherId);
        } else if (id == R.id.nav_assignments) {
            navigateToActivity(TeacherAssignmentsActivity.class, currentTeacherId);
        } else if (id == R.id.nav_profile) {
            navigateToActivity(TeacherProfileActivity.class, currentTeacherId);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // Override onBackPressed to handle closing the navigation drawer first
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Original method to navigate to another activity, used by both buttons and navigation drawer items
    // تم تعديل هذه الدالة لتقبل teacherId
    private void navigateToActivity(Class<?> activityClass, int teacherId) {
        Intent intent = new Intent(TeacherActivity.this, activityClass);
        // تمرير teacher_id إلى النشاط الجديد
        if (teacherId != -1) {
            intent.putExtra("teacher_id", teacherId);
        }
        startActivity(intent);
    }
}