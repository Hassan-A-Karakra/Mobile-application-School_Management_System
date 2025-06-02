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

         editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);

         buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = editTextMessage.getText().toString();

                if (!message.isEmpty()) {

                    Toast.makeText(CommunicateActivity.this, "Message Sent: " + message, Toast.LENGTH_SHORT).show();
                    editTextMessage.setText("");
                } else {
                    Toast.makeText(CommunicateActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
