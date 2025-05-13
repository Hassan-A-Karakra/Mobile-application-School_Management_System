package com.example.schoolmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<String> studentList;

    public StudentAdapter(List<String> studentList) {
        this.studentList = studentList;
    }

    @Override
    public StudentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StudentViewHolder holder, int position) {
        String studentName = studentList.get(position);
        holder.studentNameTextView.setText(studentName);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        public TextView studentNameTextView;

        public StudentViewHolder(View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentName);
        }
    }
}
