package com.example.schoolmanagementsystem;

import android.content.DialogInterface; // Added for AlertDialog
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem; // Import for MenuItem
import android.widget.TextView; // Added for TextView
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog; // Added for AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import android.util.Log; // Import for Log

public class TeacherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "TeacherActivity"; // Add TAG for logging
    private DrawerLayout drawerLayout;
    private int currentTeacherId; // متغير لتخزين teacher_id
    private String currentTeacherName; // متغير لتخزين اسم المعلم
    private String currentTeacherEmail; // متغير لتخزين بريد المعلم الإلكتروني
    private String currentTeacherSubject; // متغير لتخزين مادة المعلم

    private SharedPreferences sharedPreferences; // إضافة SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE); // تهيئة SharedPreferences

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        updateNavigationHeader(); // Call to update header on creation
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavigationHeader(); // Call to update header when activity resumes
    }

    private void updateNavigationHeader() {
        Log.d(TAG, "Updating navigation header..."); // Log message
        // جلب teacher_id الذي تم تمريره من TeacherLoginActivity
        // الأولوية للـ Intent، إذا لم يكن موجودًا فمن SharedPreferences
        currentTeacherId = getIntent().getIntExtra("teacher_id", -1);
        if (currentTeacherId == -1) {
            currentTeacherId = sharedPreferences.getInt("current_teacher_id", -1);
        }
        Log.d(TAG, "currentTeacherId: " + currentTeacherId); // Log message

        currentTeacherName = getIntent().getStringExtra("teacher_name");
        if (currentTeacherName == null) {
            currentTeacherName = sharedPreferences.getString("current_teacher_name", "Teacher");
        }
        Log.d(TAG, "currentTeacherName: " + currentTeacherName); // Log message

        currentTeacherEmail = getIntent().getStringExtra("teacher_email");
        if (currentTeacherEmail == null) {
            currentTeacherEmail = sharedPreferences.getString("current_teacher_email", "teacher.email@example.com");
        }
        Log.d(TAG, "currentTeacherEmail: " + currentTeacherEmail); // Log message

        currentTeacherSubject = getIntent().getStringExtra("teacher_subject");
        if (currentTeacherSubject == null) {
            currentTeacherSubject = sharedPreferences.getString("current_teacher_subject", "N/A");
        }
        Log.d(TAG, "currentTeacherSubject: " + currentTeacherSubject); // Log message

        if (currentTeacherId == -1) {
            Toast.makeText(this, "Error: Teacher ID not available. Please login again.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Teacher ID is -1. Redirecting to login."); // Log error
            Intent intent = new Intent(this, TeacherLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        // تحديث معلومات المعلم في رأس قائمة التنقل الجانبية
        NavigationView navigationView = findViewById(R.id.nav_view);
        Log.d(TAG, "Navigation View Header Count: " + navigationView.getHeaderCount()); // Log header count
        if (navigationView.getHeaderCount() > 0) {
            android.view.View headerView = navigationView.getHeaderView(0);
            TextView navTeacherName = headerView.findViewById(R.id.teacherNameTextView);
            TextView navTeacherEmail = headerView.findViewById(R.id.teacherEmailTextView);
            TextView navTeacherSubject = headerView.findViewById(R.id.teacherSubjectTextView);

            if (navTeacherName != null) {
                navTeacherName.setText(currentTeacherName);
                Log.d(TAG, "Set navTeacherName: " + currentTeacherName); // Log message
            } else {
                Log.e(TAG, "navTeacherName is null!"); // Log if null
            }
            if (navTeacherEmail != null) {
                navTeacherEmail.setText(currentTeacherEmail);
                Log.d(TAG, "Set navTeacherEmail: " + currentTeacherEmail); // Log message
            } else {
                Log.e(TAG, "navTeacherEmail is null!"); // Log if null
            }
            if (navTeacherSubject != null) {
                navTeacherSubject.setText("Subject: " + currentTeacherSubject);
                Log.d(TAG, "Set navTeacherSubject: Subject: " + currentTeacherSubject); // Log message
            } else {
                Log.e(TAG, "navTeacherSubject is null!"); // Log if null
            }
        } else {
            Log.e(TAG, "Navigation View has no header! headerCount = " + navigationView.getHeaderCount()); // Log if no header
        }
    }

    // This method is called when an item in the navigation drawer is selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_edit_profile) {
            navigateToActivity(TeacherProfileActivity.class, currentTeacherId);
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Logout")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Clear SharedPreferences on logout
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Log.d(TAG, "Logged out. SharedPreferences cleared."); // Log message

                        Intent intent = new Intent(TeacherActivity.this, TeacherLoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss(); // Dismiss the dialog
                    })
                    .show();
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
        Log.d(TAG, "Navigating to " + activityClass.getSimpleName() + " with teacher ID: " + teacherId); // Log message
    }
}