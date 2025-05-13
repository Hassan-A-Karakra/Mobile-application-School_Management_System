package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GradeInputActivity extends AppCompatActivity {

    private EditText editTextStudentName, editTextGrade;
    private Button buttonSubmitGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_input);

        // تهيئة العناصر في واجهة المستخدم
        editTextStudentName = findViewById(R.id.editTextStudentName);
        editTextGrade = findViewById(R.id.editTextGrade);
        buttonSubmitGrade = findViewById(R.id.buttonSubmitGrade);

        // التعامل مع الضغط على زر "إرسال الدرجات"
        buttonSubmitGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // الحصول على بيانات الطالب والدرجة
                String studentName = editTextStudentName.getText().toString().trim();
                String grade = editTextGrade.getText().toString().trim();

                // التحقق من صحة المدخلات
                if (!studentName.isEmpty() && !grade.isEmpty()) {
                    // هنا يمكننا إرسال البيانات إلى قاعدة البيانات أو معالجتها
                    Toast.makeText(GradeInputActivity.this, "Grade for " + studentName + " is " + grade, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GradeInputActivity.this, "Please enter both student name and grade", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // إعداد الحواف (Insets) للأشرطة العلوية والسفلية
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
