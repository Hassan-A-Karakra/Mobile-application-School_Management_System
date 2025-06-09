package com.example.schoolmanagementsystem;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable; // Import for rounded corners without drawable files
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TeacherGradesAdapter extends RecyclerView.Adapter<TeacherGradesAdapter.GradeViewHolder> {
    private List<StudentGradeItem> grades;

    public TeacherGradesAdapter(List<StudentGradeItem> grades) {
        this.grades = grades;
    }

    @Override
    public GradeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grade, parent, false);
        return new GradeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GradeViewHolder holder, int position) {
        StudentGradeItem item = grades.get(position);
        holder.subjectTextView.setText(item.getSubject());
        holder.gradeTextView.setText(item.getGrade());
        holder.teacherTextView.setText("Teacher: " + item.getTeacher());

        // Set status indicator (no drawables used)
        try {
            int gradeValue = Integer.parseInt(item.getGrade());
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(dpToPx(16, holder.itemView.getContext())); // Convert dp to px for rounded corners

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
        TextView subjectTextView, gradeTextView, teacherTextView, statusIndicator;

        GradeViewHolder(View view) {
            super(view);
            subjectTextView = view.findViewById(R.id.subjectTextView);
            gradeTextView = view.findViewById(R.id.gradeTextView);
            teacherTextView = view.findViewById(R.id.teacherTextView);
            statusIndicator = view.findViewById(R.id.statusIndicator);
        }
    }

    // Helper method to convert dp to pixels for dynamic drawables
    private int dpToPx(int dp, android.content.Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}