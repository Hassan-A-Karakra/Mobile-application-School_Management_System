package com.example.schoolmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.ViewHolder> {
    private final List<Student> studentList;

    public TeacherAttendanceAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.studentName.setText(student.getName());
        holder.absenceCount.setText("Number of absences: " + student.getAbsenceCount());
        holder.attendanceCheckBox.setChecked(student.isPresent());
        
        holder.attendanceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            student.setPresent(isChecked);
            if (!isChecked) {
                student.incrementAbsenceCount();
                holder.absenceCount.setText("Number of absences: " + student.getAbsenceCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        TextView absenceCount;
        CheckBox attendanceCheckBox;

        ViewHolder(View view) {
            super(view);
            studentName = view.findViewById(R.id.studentName);
            absenceCount = view.findViewById(R.id.absenceCount);
            attendanceCheckBox = view.findViewById(R.id.attendanceCheckBox);
        }
    }
}