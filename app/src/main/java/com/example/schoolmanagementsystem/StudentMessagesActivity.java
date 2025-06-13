package com.example.schoolmanagementsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentMessagesActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://10.0.2.2/student_system/";

    private Spinner teacherSpinner;
    private EditText messageInput;
    private Button sendButton;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> allStudentMessages = new ArrayList<>();
    private List<Message> displayedMessages = new ArrayList<>();
    private List<Teacher> teacherList = new ArrayList<>();
    private int studentId;
    private String selectedTeacherName = "";
    private ProgressBar progressBar;

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
        progressBar = findViewById(R.id.progressBar);

        adapter = new MessageAdapter(displayedMessages); // Pass the filtered list to adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchTeachers();

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) { // No need to check selectedTeacherId here
                sendMessage(message);
            } else {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });

        teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // Correctly get the original name, not the display name from the spinner
                selectedTeacherName = teacherList.get(pos).getOriginalName();
                Log.d("StudentMessages", "Spinner selected teacher: " + selectedTeacherName);
                filterAndDisplayMessages(); // Filter messages when a teacher is selected
            }
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTeacherName = ""; // No teacher selected, show all student messages (or empty)
                filterAndDisplayMessages();
            }
        });
    }

    private void fetchTeachers() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        Log.d("StudentMessages", "Fetching teachers for student ID: " + studentId);

        String url = BASE_URL + "student_get_teachers.php";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("student_id", studentId);
            Log.d("StudentMessages", "Request body: " + jsonBody.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }

                    Log.d("StudentMessages", "Response received for teachers: " + response.toString());

                    teacherList.clear();
                    List<String> teacherNamesForSpinner = new ArrayList<>();
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray teachersArray = response.getJSONArray("teachers");
                            Log.d("StudentMessages", "Number of teachers from server: " + teachersArray.length());

                            for (int i = 0; i < teachersArray.length(); i++) {
                                JSONObject obj = teachersArray.getJSONObject(i);
                                int id = obj.getInt("id");
                                String name = obj.getString("name");
                                String subject = obj.optString("subject", "");
                                String display_name = name + (subject.isEmpty() ? "" : " (" + subject + ")");

                                teacherList.add(new Teacher(id, name, subject)); // Store original name
                                teacherNamesForSpinner.add(display_name); // Add display name for spinner
                                Log.d("StudentMessages", "Teacher added: ID=" + id + ", Name='" + name + "', Subject='" + subject + "', Display='" + display_name + "'");
                            }

                            if (!teacherList.isEmpty()) {
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                                        android.R.layout.simple_spinner_dropdown_item, teacherNamesForSpinner);
                                teacherSpinner.setAdapter(spinnerAdapter);

                                // Set initial selection and load messages
                                // Ensure we set selectedTeacherName from the original name of the first teacher
                                selectedTeacherName = teacherList.get(0).getOriginalName();
                                Log.d("StudentMessages", "Initial selected teacher name: " + selectedTeacherName);
                                loadAllStudentMessages();
                            } else {
                                Toast.makeText(this, "No teachers available", Toast.LENGTH_SHORT).show();
                                Log.d("StudentMessages", "No teachers available.");
                            }
                        } else {
                            String errorMessage = response.optString("message", "Failed to load teachers");
                            Log.e("StudentMessages", "Error response from teachers API: " + errorMessage);
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("StudentMessages", "JSON parsing error for teachers: " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing teacher data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            String errorMessage = "Failed to load teachers";
            if (error.networkResponse != null) {
                try {
                    String errorResponse = new String(error.networkResponse.data);
                    Log.e("StudentMessages", "Network error response for teachers: " + errorResponse);
                    JSONObject errorJson = new JSONObject(errorResponse);
                    errorMessage = errorJson.optString("message", errorMessage);
                } catch (Exception e) {
                    Log.e("StudentMessages", "Error parsing teachers network error response: " + e.getMessage());
                    errorMessage += ": " + new String(error.networkResponse.data);
                }
            } else {
                Log.e("StudentMessages", "Volley network error for teachers: " + error.toString());
            }
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(this).add(request);
    }

    private void loadAllStudentMessages() {
        String url = BASE_URL + "student_get_messages.php";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("student_id", studentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    allStudentMessages.clear();
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray messagesArray = response.getJSONArray("messages");
                            Log.d("StudentMessages", "Received " + messagesArray.length() + " messages from server for student ID " + studentId);
                            for (int i = 0; i < messagesArray.length(); i++) {
                                JSONObject obj = messagesArray.getJSONObject(i);
                                String title = obj.getString("title");
                                String messageContent = obj.getString("message");
                                String timestamp = obj.getString("created_at");

                                boolean isSentByMe = title.startsWith("Message from Student"); // Check if title starts with this
                                Log.d("StudentMessages", "Processing message: Title='" + title + "', isSentByMe=" + isSentByMe);
                                allStudentMessages.add(new Message(title, messageContent, timestamp, isSentByMe));
                            }
                            Log.d("StudentMessages", "Total messages loaded into allStudentMessages: " + allStudentMessages.size());
                            filterAndDisplayMessages();
                        } else {
                            String errorMessage = response.getString("message");
                            Log.e("StudentMessages", "Error from messages API: " + errorMessage);
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("StudentMessages", "JSON parsing error for messages: " + e.getMessage());
                        Toast.makeText(this, "Error parsing messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            String errorMessage = "Failed to load messages";
            if (error.networkResponse != null) {
                errorMessage += ": " + new String(error.networkResponse.data);
            }
            Log.e("StudentMessages", "Volley network error for messages: " + errorMessage);
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    private void filterAndDisplayMessages() {
        displayedMessages.clear();

        Log.d("StudentMessages", "Starting filter for selected teacher: '" + selectedTeacherName + "' (trimmed)");
        Log.d("StudentMessages", "Total messages in allStudentMessages before filter: " + allStudentMessages.size());

        for (Message msg : allStudentMessages) {
            Log.d("StudentMessages", "Filtering check: Message Title='" + msg.getTitle() + "' (trimmed: '" + msg.getTitle().trim() + "'), isSentByMe: " + msg.isSentByMe());

            if (msg.isSentByMe()) {
                displayedMessages.add(msg);
                Log.d("StudentMessages", "Adding student's own message.");
            } else {
                // If the message is from a teacher, check if its title (trimmed and case-insensitive)
                // matches the selected teacher's original name (trimmed and case-insensitive)
                if (msg.getTitle().trim().equalsIgnoreCase(selectedTeacherName.trim())) {
                    displayedMessages.add(msg);
                    Log.d("StudentMessages", "Adding teacher's message matching: '" + selectedTeacherName + "'");
                } else {
                    Log.d("StudentMessages", "Skipping teacher's message: '" + msg.getTitle() + "' does NOT match '" + selectedTeacherName + "'");
                }
            }
        }

        Log.d("StudentMessages", "Total messages in displayedMessages after filter: " + displayedMessages.size());

        adapter.notifyDataSetChanged();
        if (!displayedMessages.isEmpty()) {
            recyclerView.scrollToPosition(displayedMessages.size() - 1);
        }
    }

    private void sendMessage(String content) {
        String url = BASE_URL + "student_send_message.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getBoolean("success")) {
                            messageInput.setText("");
                            loadAllStudentMessages(); // Reload all messages and then filter
                            Toast.makeText(this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to send message: " + jsonResponse.optString("message", "Unknown error"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("StudentMessages", "Error parsing send message response: " + e.getMessage());
                        Toast.makeText(this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    String errorMessage = "Failed to send message";
                    if (error.networkResponse != null) {
                        try {
                            String errorResponse = new String(error.networkResponse.data);
                            Log.e("StudentMessages", "Network error response for send message: " + errorResponse);
                            JSONObject errorJson = new JSONObject(errorResponse);
                            errorMessage = errorJson.optString("message", errorMessage);
                        } catch (Exception e) {
                            Log.e("StudentMessages", "Error parsing send message network error response: " + e.getMessage());
                            errorMessage += ": " + new String(error.networkResponse.data);
                        }
                    } else {
                        Log.e("StudentMessages", "Volley network error for send message: " + error.toString());
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", String.valueOf(studentId));
                // It seems student_send_message.php expects teacher_id and message content
                // based on previous analysis of the PHP side.
                // Ensure selectedTeacherId is valid before sending.
                if (selectedTeacherName != null && !selectedTeacherName.isEmpty() && !teacherList.isEmpty()) {
                    // Find the ID for the selected teacher name
                    int teacherIdToSend = -1;
                    for (Teacher teacher : teacherList) {
                        if (teacher.getOriginalName().equalsIgnoreCase(selectedTeacherName)) {
                            teacherIdToSend = teacher.getId();
                            break;
                        }
                    }
                    if (teacherIdToSend != -1) {
                        params.put("teacher_id", String.valueOf(teacherIdToSend));
                    } else {
                        Log.e("StudentMessages", "Could not find teacher ID for selected teacher name: " + selectedTeacherName);
                        // Handle error, maybe don't send message or show a toast
                    }
                } else {
                    Log.e("StudentMessages", "No teacher selected or teacher list is empty when trying to send message.");
                }
                params.put("content", content);
                // Also sending 'title' to match what the PHP script expects, even though it's
                // primarily used for *teacher* messages and will be overridden by teacher's name.
                // For student messages, this title isn't critical for filtering, but good to send.
                params.put("title", "Message from Student " + studentId);
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    // Helper class for Teacher object to store original name for filtering
    private static class Teacher {
        private final int id;
        private final String originalName; // Stores "Mr. Hassan"
        private final String subject;

        public Teacher(int id, String originalName, String subject) {
            this.id = id;
            this.originalName = originalName;
            this.subject = subject;
        }

        public int getId() {
            return id;
        }

        public String getOriginalName() {
            return originalName;
        }

        public String getSubject() {
            return subject;
        }

        // This method will be used for display in the spinner
        @Override
        public String toString() {
            return originalName + " (" + subject + ")";
        }
    }
}