package com.example.schoolmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    private List<Student> students;

    public AttendanceAdapter(List<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = students.get(position);
        holder.textViewName.setText(student.getName());
        holder.checkBoxPresent.setChecked(student.isPresent());
        holder.textViewAbsenceCount.setText(String.valueOf(student.getAbsenceCount()));

        holder.checkBoxPresent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            student.setPresent(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        CheckBox checkBoxPresent;
        TextView textViewAbsenceCount;

        ViewHolder(View view) {
            super(view);
            textViewName = view.findViewById(R.id.textViewName);
            checkBoxPresent = view.findViewById(R.id.checkBoxPresent);
            textViewAbsenceCount = view.findViewById(R.id.textViewAbsenceCount);
        }
    }
}