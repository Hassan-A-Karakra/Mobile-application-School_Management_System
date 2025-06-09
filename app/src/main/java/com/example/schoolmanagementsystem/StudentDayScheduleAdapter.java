package com.example.schoolmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentDayScheduleAdapter extends RecyclerView.Adapter<StudentDayScheduleAdapter.DayViewHolder> {
    private List<StudentDaySchedule> studentDaySchedules;
    public StudentDayScheduleAdapter(List<StudentDaySchedule> studentDaySchedules) {
        this.studentDaySchedules = studentDaySchedules;
    }
    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_day_schedule, parent, false);
        return new DayViewHolder(view);
    }
    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        StudentDaySchedule studentDaySchedule = studentDaySchedules.get(position);
        holder.dayTextView.setText(studentDaySchedule.day);
        StudentClassSessionAdapter sessionAdapter = new StudentClassSessionAdapter(studentDaySchedule.sessions);
        holder.sessionsRecyclerView.setAdapter(sessionAdapter);
    }
    @Override
    public int getItemCount() { return studentDaySchedules.size(); }
    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView;
        RecyclerView sessionsRecyclerView;
        DayViewHolder(View view) {
            super(view);
            dayTextView = view.findViewById(R.id.dayTextView);
            sessionsRecyclerView = view.findViewById(R.id.sessionsRecyclerView);
            sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }
    }
}