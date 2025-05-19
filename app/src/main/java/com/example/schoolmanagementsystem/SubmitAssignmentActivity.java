package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SubmitAssignmentActivity extends AppCompatActivity {

    private Spinner spinnerAssignments;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_assignment);

        spinnerAssignments = findViewById(R.id.spinnerAssignments);
        buttonSubmit = findViewById(R.id.buttonSubmitNow);

        // 📝 Example assignments (in production, you could load this from DB or intent extras)
        List<String> assignments = new ArrayList<>();
        assignments.add("رياضيات: صفحة 45 - تمارين 1-5");
        assignments.add("علوم: بحث عن الطاقة");
        assignments.add("لغة عربية: تعبير عن الوطن");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                assignments
        );
        spinnerAssignments.setAdapter(adapter);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAssignment = spinnerAssignments.getSelectedItem().toString();
                Toast.makeText(SubmitAssignmentActivity.this,
                        "تم تسليم الواجب: " + selectedAssignment,
                        Toast.LENGTH_LONG).show();

                // You can add file upload or DB entry here
            }
        });
    }}
