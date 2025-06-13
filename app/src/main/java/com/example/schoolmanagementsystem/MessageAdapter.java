package com.example.schoolmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSender, textContent, textTimestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            textSender = itemView.findViewById(R.id.textSender);
            textContent = itemView.findViewById(R.id.textContent);
            textTimestamp = itemView.findViewById(R.id.textTimestamp);
        }
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messages.get(position);

        String senderPrefix = message.getSender().equalsIgnoreCase("student") ? "You" : "Teacher";
        holder.textSender.setText(senderPrefix);
        holder.textContent.setText(message.getContent());
        holder.textTimestamp.setText(message.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
