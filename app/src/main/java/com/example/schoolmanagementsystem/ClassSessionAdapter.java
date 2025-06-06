package com.example.schoolmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ClassSessionAdapter extends RecyclerView.Adapter<ClassSessionAdapter.SessionViewHolder> {
    private List<ClassSession> sessions;
    public ClassSessionAdapter(List<ClassSession> sessions) {
        this.sessions = sessions;
    }
    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_session, parent, false);
        return new SessionViewHolder(view);
    }
    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        ClassSession session = sessions.get(position);
        holder.subjectTextView.setText(session.subject);
        holder.timeTextView.setText(session.time);
        holder.teacherTextView.setText(session.teacher != null ? session.teacher : "");
    }
    @Override
    public int getItemCount() { return sessions.size(); }
    static class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView, timeTextView, teacherTextView;
        SessionViewHolder(View view) {
            super(view);
            subjectTextView = view.findViewById(R.id.subjectTextView);
            timeTextView = view.findViewById(R.id.timeTextView);
            teacherTextView = view.findViewById(R.id.teacherTextView);
        }
    }
}