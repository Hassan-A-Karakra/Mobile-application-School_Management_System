package com.example.schoolmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DayScheduleAdapter extends RecyclerView.Adapter<DayScheduleAdapter.DayViewHolder> {
    private List<DaySchedule> daySchedules;
    public DayScheduleAdapter(List<DaySchedule> daySchedules) {
        this.daySchedules = daySchedules;
    }
    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_schedule, parent, false);
        return new DayViewHolder(view);
    }
    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        DaySchedule daySchedule = daySchedules.get(position);
        holder.dayTextView.setText(daySchedule.day);
        ClassSessionAdapter sessionAdapter = new ClassSessionAdapter(daySchedule.sessions);
        holder.sessionsRecyclerView.setAdapter(sessionAdapter);
    }
    @Override
    public int getItemCount() { return daySchedules.size(); }
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