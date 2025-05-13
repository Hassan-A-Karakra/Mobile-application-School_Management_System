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

public class CommunicateActivity extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);

        // تهيئة الـ EditText وزر الإرسال
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);

        // إضافة تفاعل مع زر "إرسال"
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();

                // التحقق من أن الرسالة ليست فارغة
                if (!message.isEmpty()) {
                    // يمكنك إرسال الرسالة هنا باستخدام وسيلة التواصل المناسبة (مثل البريد الإلكتروني أو الرسائل)
                    // حاليًا سنعرض رسالة مؤقتة فقط عبر Toast
                    Toast.makeText(CommunicateActivity.this, "Message Sent: " + message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommunicateActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // إعداد الـ Insets (للتأكد من التعامل مع الأشرطة العلوية والسفلية بشكل صحيح)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
