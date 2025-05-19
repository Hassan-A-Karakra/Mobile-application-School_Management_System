package com.example.schoolmanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {

    private List<Assignment> assignments;
    private Context context;

    public AssignmentAdapter(Context context, List<Assignment> assignments) {
        this.context = context;
        this.assignments = assignments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assignment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);
        holder.subject.setText(assignment.getSubject());
        holder.description.setText(assignment.getDescription());
        holder.dueDate.setText("Due: " + assignment.getDueDate());
        holder.submitButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubmitAssignmentActivity.class);
            intent.putExtra("subject", assignment.getSubject());
            intent.putExtra("description", assignment.getDescription());
            intent.putExtra("dueDate", assignment.getDueDate());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subject, description, dueDate;
        Button submitButton;

        public ViewHolder(View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.assignment_subject);
            description = itemView.findViewById(R.id.assignment_description);
            dueDate = itemView.findViewById(R.id.assignment_due_date);
            submitButton = itemView.findViewById(R.id.button_submit_assignment);
        }
    }

}
