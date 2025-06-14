package com.example.schoolmanagementsystem;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentMessageAdapter extends RecyclerView.Adapter<StudentMessageAdapter.ViewHolder> {

    private final List<StudentMessage> messages;

     private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public StudentMessageAdapter(List<StudentMessage> messages) {
        this.messages = messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageContentTextView, timestampTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            messageContentTextView = itemView.findViewById(R.id.messageContentTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // استخدام isSentByMe لتحديد نوع العرض
        return messages.get(position).isSentByMe() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public StudentMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentMessageAdapter.ViewHolder holder, int position) {
        StudentMessage message = messages.get(position);

        holder.messageContentTextView.setText(message.getMessageContent());
         holder.timestampTextView.setText(message.getTimestamp());

     }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}