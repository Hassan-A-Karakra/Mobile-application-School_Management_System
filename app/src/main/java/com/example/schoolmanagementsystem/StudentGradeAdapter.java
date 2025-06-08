package com.example.schoolmanagementsystem;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentGradeAdapter extends RecyclerView.Adapter<StudentGradeAdapter.StudentGradeViewHolder> {

    private List<Student> studentList;

    public StudentGradeAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentGradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_grade, parent, false);
        return new StudentGradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentGradeViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.textViewStudentNameItem.setText(student.getName());
        holder.editTextGradeItem.setText(student.getGrade());

        // Set a TextWatcher to update the student's grade in the list when the text changes
        holder.editTextGradeItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                student.setGrade(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentGradeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStudentNameItem;
        EditText editTextGradeItem;

        public StudentGradeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStudentNameItem = itemView.findViewById(R.id.textViewStudentNameItem);
            editTextGradeItem = itemView.findViewById(R.id.editTextGradeItem);
        }
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> newStudentList) {
        this.studentList = newStudentList;
        notifyDataSetChanged();
    }
} 