package com.example.schoolmanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAssignmentAdapter extends RecyclerView.Adapter<StudentAssignmentAdapter.ViewHolder> {

    public interface OnAssignmentClickListener {
        void onAssignmentClick(Assignment assignment);
    }

    private Context context;
    private List<Assignment> assignments;
    private OnAssignmentClickListener listener;

    public StudentAssignmentAdapter(Context context, List<Assignment> assignments, OnAssignmentClickListener listener) {
        this.context = context;
        this.assignments = assignments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);
        holder.title.setText(assignment.getTitle());
        holder.desc.setText(assignment.getDescription());
        holder.due.setText("Due: " + assignment.getDueDate());

        // Show completion indicator if assignment is submitted
        if (assignment.isSubmitted()) {
            holder.completionIndicator.setVisibility(View.VISIBLE);
            holder.submitButton.setVisibility(View.GONE);
        } else {
            holder.completionIndicator.setVisibility(View.GONE);
            holder.submitButton.setVisibility(View.VISIBLE);
        }

        // Clicking the whole item view also works
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAssignmentClick(assignment);
            }
        });

        // Clicking the "Submit" button
        holder.submitButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAssignmentClick(assignment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, due;
        Button submitButton;
        LinearLayout completionIndicator;
        ImageView checkmarkIcon;
        TextView completedText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.assignmentTitle);
            desc = itemView.findViewById(R.id.assignmentDescription);
            due = itemView.findViewById(R.id.dueDate);
            submitButton = itemView.findViewById(R.id.button_submit_assignment);
            completionIndicator = itemView.findViewById(R.id.completionIndicator);
            checkmarkIcon = itemView.findViewById(R.id.checkmarkIcon);
            completedText = itemView.findViewById(R.id.completedText);
        }
    }
}
