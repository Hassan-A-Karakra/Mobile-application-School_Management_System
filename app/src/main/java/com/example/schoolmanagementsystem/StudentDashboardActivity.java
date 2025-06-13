package com.example.schoolmanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

public class StudentDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    private TextView textWelcome;
    private MaterialButton btnSchedule, btnGrades, btnAssignments, btnCommunicate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int studentId = prefs.getInt("student_id", -1);

        if (studentId == -1) {
            startActivity(new Intent(this, StudentLoginActivity.class));
            finish();
            return;
        }

        String studentName = prefs.getString("student_name", "Unknown");
        String studentClass = prefs.getString("student_class", "N/A");

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        textWelcome = findViewById(R.id.textWelcome);
        textWelcome.setText("Welcome, " + studentName);

        btnSchedule = findViewById(R.id.btnSchedule);
        btnGrades = findViewById(R.id.btnGrades);
        btnAssignments = findViewById(R.id.btnAssignments);
        btnCommunicate = findViewById(R.id.btnCommunicate);

        btnSchedule.setOnClickListener(v -> startActivity(new Intent(this, StudentScheduleActivity.class)));
        btnGrades.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudentGradesActivity.class);
            intent.putExtra("student_id", studentId);
            startActivity(intent);
        });
        btnAssignments.setOnClickListener(v -> startActivity(new Intent(this, StudentAssignmentsActivity.class)));
        btnCommunicate.setOnClickListener(v -> startActivity(new Intent(this, StudentMessagesActivity.class)));

        // Drawer header content
        View headerView = navigationView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.navStudentName);
        TextView navId = headerView.findViewById(R.id.navStudentId);
        TextView navClass = headerView.findViewById(R.id.navStudentClass);
        ImageView navImage = headerView.findViewById(R.id.navStudentImage);

        navName.setText(studentName);
        navId.setText("ID: " + studentId);
        navClass.setText("Class: " + studentClass);

        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_edit_info) {
                startActivity(new Intent(this, StudentEditStudentInfoActivity.class));
            } else if (itemId == R.id.nav_logout) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(this, StudentLoginActivity.class));
                finish();
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}