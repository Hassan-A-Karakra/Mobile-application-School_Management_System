package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.*;
import org.json.*;
import java.util.*;

public class StudentMessagesActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://10.0.2.2/student_system/";
    private Spinner teacherSpinner;
    private EditText messageInput;
    private Button sendButton;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messages = new ArrayList<>();
    private List<Teacher> teacherList = new ArrayList<>();
    private int studentId;
    private int selectedTeacherId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_messages);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        studentId = prefs.getInt("student_id", -1);
        if (studentId == -1) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        teacherSpinner = findViewById(R.id.teacherSpinner);
        messageInput = findViewById(R.id.editMessage);
        sendButton = findViewById(R.id.btnSendMessage);
        recyclerView = findViewById(R.id.recyclerViewMessages);

        adapter = new MessageAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchTeachers();

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (selectedTeacherId != -1 && !message.isEmpty()) {
                sendMessage(message);
            } else {
                Toast.makeText(this, "Please select a teacher and enter a message", Toast.LENGTH_SHORT).show();
            }
        });

        teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedTeacherId = teacherList.get(pos).getId();
                loadMessages();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchTeachers() {
        String url = BASE_URL + "student_get_teachers_for_student.php?student_id=" + studentId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    teacherList.clear();
                    List<String> teacherNames = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.optJSONObject(i);
                        if (obj != null) {
                            int id = obj.optInt("id");
                            String name = obj.optString("name");
                            teacherList.add(new Teacher(id, name));
                            teacherNames.add(name);
                        }
                    }
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_dropdown_item, teacherNames);
                    teacherSpinner.setAdapter(spinnerAdapter);
                    if (!teacherList.isEmpty()) {
                        selectedTeacherId = teacherList.get(0).getId();
                        loadMessages();
                    }
                }, error -> Toast.makeText(this, "Failed to load teachers", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(request);
    }

    private void loadMessages() {
        String url = BASE_URL + "student_get_conversation.php?student_id=" + studentId + "&teacher_id=" + selectedTeacherId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    messages.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.optJSONObject(i);
                        if (obj != null) {
                            messages.add(new Message(
                                    obj.optString("sender"),
                                    obj.optString("content"),
                                    obj.optString("timestamp")
                            ));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (!messages.isEmpty()) {
                        recyclerView.scrollToPosition(messages.size() - 1);
                    }
                }, error -> Toast.makeText(this, "Failed to load messages", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(request);
    }

    private void sendMessage(String content) {
        String url = BASE_URL + "student_send_message.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    messageInput.setText("");
                    loadMessages();
                },
                error -> Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", String.valueOf(studentId));
                params.put("teacher_id", String.valueOf(selectedTeacherId));
                params.put("sender_type", "student");
                params.put("content", content);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
}