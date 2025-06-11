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

    // Original Button declarations - NOW RESTORED
    Button buttonStudentList, buttonGradeInput, buttonAttendance,
            buttonCommunicate, buttonSchedule, buttonAssignments, buttonProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

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

        buttonStudentList.setOnClickListener(v -> navigateToActivity(TeacherStudentListActivity.class));
        buttonGradeInput.setOnClickListener(v -> navigateToActivity(TeacherGradeInputActivity.class));
        buttonAttendance.setOnClickListener(v -> navigateToActivity(TeacherAttendanceActivity.class));
        buttonCommunicate.setOnClickListener(v -> navigateToActivity(TeacherCommunicateActivity.class));
        buttonSchedule.setOnClickListener(v -> navigateToActivity(StudentScheduleActivity.class));
        buttonAssignments.setOnClickListener(v -> navigateToActivity(TeacherAssignmentsActivity.class));
        //buttonProfile.setOnClickListener(v -> navigateToActivity(TeacherProfileActivity.class)); // This line is still commented out if it was before, assuming it's not implemented yet.
        // --- END RESTORED: Original Button Initializations and Listeners ---
    }

    // This method is called when an item in the navigation drawer is selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_student_list) {
            navigateToActivity(RegisterStudentListActivity.class);
        } else if (id == R.id.nav_grade_input) {
            navigateToActivity(TeacherGradeInputActivity.class);
        } else if (id == R.id.nav_attendance) {
            navigateToActivity(TeacherAttendanceActivity.class);
        } else if (id == R.id.nav_communicate) {
            navigateToActivity(TeacherCommunicateActivity.class);
        } else if (id == R.id.nav_schedule) {
            navigateToActivity(StudentScheduleActivity.class);
        } else if (id == R.id.nav_assignments) {
            navigateToActivity(TeacherAssignmentsActivity.class);
        } else if (id == R.id.nav_profile) {
            navigateToActivity(TeacherProfileActivity.class);
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
    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(TeacherActivity.this, activityClass);
        startActivity(intent);
    }
}