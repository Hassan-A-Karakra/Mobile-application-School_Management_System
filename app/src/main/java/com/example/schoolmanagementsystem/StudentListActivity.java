package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StudentListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // إعداد الحواف (Insets) للأشرطة العلوية والسفلية
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // التعامل مع الضغط على زر عرض الطلاب
        Button buttonShowStudents = findViewById(R.id.buttonShowStudents);
        buttonShowStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // يمكنك إضافة وظيفة لعرض قائمة الطلاب هنا (مثال: عرض Toast)
                Toast.makeText(StudentListActivity.this, "Displaying Student List", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
