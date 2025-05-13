package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CommunicateActivity extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);

        // ربط العناصر
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);

        // عند الضغط على زر "Send Message"
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // الحصول على النص المدخل في EditText
                String message = editTextMessage.getText().toString();

                if (!message.isEmpty()) {
                    // إرسال الرسالة (مثال: عرض رسالة Toast أو حفظ البيانات)
                    Toast.makeText(CommunicateActivity.this, "Message Sent: " + message, Toast.LENGTH_SHORT).show();
                    editTextMessage.setText("");  // مسح النص بعد الإرسال
                } else {
                    // إذا كان النص فارغًا
                    Toast.makeText(CommunicateActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
