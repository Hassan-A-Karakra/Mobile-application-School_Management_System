package com.example.schoolmanagementsystem;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AssignmentViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AssignmentAdapter adapter;
    private List<Assignment> assignmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_view);

        recyclerView = findViewById(R.id.recyclerViewAssignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Temporary sample data
        assignmentList = new ArrayList<>();
        assignmentList.add(new Assignment("رياضيات", "صفحة 45، تمارين 1-5", "2025-05-25"));
        assignmentList.add(new Assignment("علوم", "بحث عن الطاقة", "2025-05-27"));
        assignmentList.add(new Assignment("لغة عربية", "كتابة تعبير عن الوطن", "2025-05-30"));

        adapter = new AssignmentAdapter(this, assignmentList);
        recyclerView.setAdapter(adapter);
    }
}
