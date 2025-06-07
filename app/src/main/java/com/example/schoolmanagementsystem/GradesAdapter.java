package com.example.schoolmanagementsystem;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.GradeViewHolder> {
    private List<GradeItem> grades;

    public GradesAdapter(List<GradeItem> grades) {
        this.grades = grades;
    }

    @Override
    public GradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GradeViewHolder holder, int position) {
        GradeItem item = grades.get(position);
        holder.subjectTextView.setText(item.getSubject());
        holder.gradeTextView.setText(item.getGrade());
        holder.teacherTextView.setText("Teacher: " + item.getTeacher());

        try {
            int gradeValue = Integer.parseInt(item.getGrade());
            if (gradeValue >= 60) {
                holder.statusIndicator.setText("Passing");
                holder.statusIndicator.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
            } else {
                holder.statusIndicator.setText("Failing");
                holder.statusIndicator.setBackgroundColor(Color.parseColor("#F44336")); // Red
            }
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
        TextView subjectTextView, gradeTextView, teacherTextView, statusIndicator;

        GradeViewHolder(View view) {
            super(view);
            subjectTextView = view.findViewById(R.id.subjectTextView);
            gradeTextView = view.findViewById(R.id.gradeTextView);
            teacherTextView = view.findViewById(R.id.teacherTextView);
            statusIndicator = view.findViewById(R.id.statusIndicator);
        }
    }
}