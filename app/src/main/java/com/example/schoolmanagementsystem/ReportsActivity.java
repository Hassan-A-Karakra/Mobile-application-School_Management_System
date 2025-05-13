package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportsActivity extends AppCompatActivity {

    private Button buttonGenerateReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // تهيئة زر "إنشاء تقرير"
        buttonGenerateReport = findViewById(R.id.buttonGenerateReport);

        // التعامل مع الضغط على زر "إنشاء تقرير"
        buttonGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // هنا يمكننا إنشاء التقرير باستخدام بيانات معينة (مثلاً درجات الطلاب)
                // في الوقت الحالي نعرض رسالة لتوضيح النتيجة
                Toast.makeText(ReportsActivity.this, "Report Generated", Toast.LENGTH_SHORT).show();
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
