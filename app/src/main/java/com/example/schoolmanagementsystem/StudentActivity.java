package com.example.schoolmanagementsystem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.ViewGroup;


public class StudentActivity extends AppCompatActivity {
    private RecyclerView scheduleRecyclerView;
    private TextView gradesTextView;
    private Button selectFileButton;
    private TextView selectedFileNameTextView;
    private EditText messageEditText;
    private Button sendMessageButton;
    private Uri selectedFileUri;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedFileUri = result.getData().getData();
                    if (selectedFileUri != null) {
                        selectedFileNameTextView.setText(selectedFileUri.getLastPathSegment());
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
        gradesTextView = findViewById(R.id.gradesTextView);
        selectFileButton = findViewById(R.id.selectFileButton);
        selectedFileNameTextView = findViewById(R.id.selectedFileNameTextView);
        messageEditText = findViewById(R.id.messageEditText);
        sendMessageButton = findViewById(R.id.sendMessageButton);

        setupScheduleRecyclerView();
        setupFileUpload();
        setupMessaging();
    }

    private void setupScheduleRecyclerView() {
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ScheduleItem> scheduleItems = getDummyScheduleData();
        ScheduleAdapter adapter = new ScheduleAdapter(scheduleItems);
        scheduleRecyclerView.setAdapter(adapter);
    }

    private List<ScheduleItem> getDummyScheduleData() {
        List<ScheduleItem> items = new ArrayList<>();
        items.add(new ScheduleItem("الرياضيات", "9:00 - 10:30", "أ. أحمد محمد"));
        items.add(new ScheduleItem("العلوم", "10:45 - 12:15", "أ. سارة علي"));
        items.add(new ScheduleItem("اللغة العربية", "12:30 - 2:00", "أ. محمد خالد"));
        return items;
    }

    private void setupFileUpload() {
        selectFileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            filePickerLauncher.launch(intent);
        });
    }

    private void setupMessaging() {
        sendMessageButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                Toast.makeText(this, "تم إرسال الرسالة بنجاح", Toast.LENGTH_SHORT).show();
                messageEditText.setText("");
            } else {
                Toast.makeText(this, "الرجاء كتابة رسالة", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class ScheduleItem {
    private String subject;
    private String time;
    private String teacher;

    public ScheduleItem(String subject, String time, String teacher) {
        this.subject = subject;
        this.time = time;
        this.teacher = teacher;
    }

    public String getSubject() { return subject; }
    public String getTime() { return time; }
    public String getTeacher() { return teacher; }
}

class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<ScheduleItem> items;

    public ScheduleAdapter(List<ScheduleItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleItem item = items.get(position);
        holder.subjectTextView.setText(item.getSubject());
        holder.timeTextView.setText(item.getTime());
        holder.teacherTextView.setText(item.getTeacher());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView;
        TextView timeTextView;
        TextView teacherTextView;

        ViewHolder(View view) {
            super(view);
            subjectTextView = view.findViewById(R.id.subjectTextView);
            timeTextView = view.findViewById(R.id.timeTextView);
            teacherTextView = view.findViewById(R.id.teacherTextView);
        }
    }
}