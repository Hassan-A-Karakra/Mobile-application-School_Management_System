package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Import for MenuItem
import android.widget.TextView; // Added for TextView

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
    private String currentTeacherName; // متغير لتخزين اسم المعلم
    private String currentTeacherEmail; // متغير لتخزين بريد المعلم الإلكتروني
    private String currentTeacherSubject; // متغير لتخزين مادة المعلم

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        // جلب teacher_id الذي تم تمريره من TeacherLoginActivity
        currentTeacherId = getIntent().getIntExtra("teacher_id", -1);
        currentTeacherName = getIntent().getStringExtra("teacher_name");
        currentTeacherEmail = getIntent().getStringExtra("teacher_email");
        currentTeacherSubject = getIntent().getStringExtra("teacher_subject");

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

        // تحديث معلومات المعلم في رأس قائمة التنقل الجانبية
        if (navigationView.getHeaderCount() > 0) {
            android.view.View headerView = navigationView.getHeaderView(0);
            TextView navTeacherName = headerView.findViewById(R.id.nav_teacher_name);
            TextView navTeacherEmail = headerView.findViewById(R.id.nav_teacher_email);
            TextView navTeacherSubject = headerView.findViewById(R.id.nav_teacher_subject);

            if (navTeacherName != null && currentTeacherName != null) {
                navTeacherName.setText(currentTeacherName);
            }
            if (navTeacherEmail != null && currentTeacherEmail != null) {
                navTeacherEmail.setText(currentTeacherEmail);
            }
            if (navTeacherSubject != null && currentTeacherSubject != null) {
                navTeacherSubject.setText("Subject: " + currentTeacherSubject);
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    // This method is called when an item in the navigation drawer is selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            // توجيه المستخدم إلى شاشة تسجيل الدخول (TeacherLoginActivity)
            Intent intent = new Intent(TeacherActivity.this, TeacherLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // إغلاق النشاط الحالي
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