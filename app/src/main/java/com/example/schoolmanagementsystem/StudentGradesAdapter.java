package com.example.schoolmanagementsystem;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentGradesAdapter extends RecyclerView.Adapter<StudentGradesAdapter.GradeViewHolder> {
    private List<StudentGradeItem> grades;

    public StudentGradesAdapter(List<StudentGradeItem> grades) {
        this.grades = grades;
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        StudentGradeItem item = grades.get(position);
        holder.subjectTextView.setText(item.getSubject());
        holder.gradeTextView.setText("Grade: " + item.getGrade());
        holder.absencesTextView.setText("Absences: " + item.getAbsences());
        holder.teacherNameTextView.setText("Teacher: " + item.getTeacherName());

        // Set status indicator
        try {
            int gradeValue = Integer.parseInt(item.getGrade());
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(dpToPx(16, holder.itemView.getContext()));

            if (gradeValue >= 60) {
                holder.statusIndicator.setText("Passing");
                bg.setColor(Color.parseColor("#4CAF50")); // Green
            } else {
                holder.statusIndicator.setText("Failing");
                bg.setColor(Color.parseColor("#F44336")); // Red
            }
            holder.statusIndicator.setBackground(bg);
        } catch (NumberFormatException e) {
            holder.statusIndicator.setText("N/A");
            holder.statusIndicator.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView, gradeTextView, absencesTextView, teacherNameTextView, statusIndicator;

        GradeViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subjectTextView);
            gradeTextView = itemView.findViewById(R.id.gradeTextView);
            absencesTextView = itemView.findViewById(R.id.absencesTextView);
            teacherNameTextView = itemView.findViewById(R.id.teacherNameTextView);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }
    }

    private int dpToPx(int dp, android.content.Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}