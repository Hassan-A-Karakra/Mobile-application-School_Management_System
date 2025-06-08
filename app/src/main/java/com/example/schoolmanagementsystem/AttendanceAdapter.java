package com.example.schoolmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<Student> studentList;

    public AttendanceAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item_attendance, parent, false);
        return new AttendanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.studentNameTextView.setText(student.getName());
       // holder.attendanceCheckBox.setChecked(student.isPresent());

        // عند تغيير حالة الحضور، نقوم بتحديث القيمة في القائمة
        holder.attendanceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
         //   student.setPresent(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        public TextView studentNameTextView;
        public CheckBox attendanceCheckBox;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            studentNameTextView = itemView.findViewById(R.id.studentName);
            attendanceCheckBox = itemView.findViewById(R.id.attendanceCheckBox);
        }
    }
}
