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

public class TeacherStudentGradeAdapter extends RecyclerView.Adapter<TeacherStudentGradeAdapter.StudentGradeViewHolder> {

    private List<Student> studentList;

    public TeacherStudentGradeAdapter(List<Student> studentList) {
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
        holder.studentNameTextView.setText(student.getName());

        // Remove previous TextWatcher to prevent infinite loops or incorrect updates
        if (holder.gradeEditText.getTag() instanceof TextWatcher) {
            holder.gradeEditText.removeTextChangedListener((TextWatcher) holder.gradeEditText.getTag());
        }

        holder.gradeEditText.setText(student.getScore());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                student.setScore(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        holder.gradeEditText.addTextChangedListener(textWatcher);
        holder.gradeEditText.setTag(textWatcher); // Store TextWatcher as a tag
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentGradeViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameTextView;
        EditText gradeEditText;

        public StudentGradeViewHolder(@NonNull View itemView) {
            super(itemView);

            studentNameTextView = itemView.findViewById(R.id.studentNameTextView);
            gradeEditText = itemView.findViewById(R.id.gradeEditText);
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