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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSender, textContent, textTimestamp;
        LinearLayout messageBubbleLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textSender = itemView.findViewById(R.id.textSender);
            textContent = itemView.findViewById(R.id.textContent);
            textTimestamp = itemView.findViewById(R.id.textTimestamp);
            // Ensure the root of item_message.xml is a LinearLayout when casting
            messageBubbleLayout = (LinearLayout) itemView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isSentByMe() ? 0 : 1;
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

        holder.textContent.setText(message.getMessageContent());
        holder.textTimestamp.setText(message.getTimestamp());

        // Create new LinearLayout.LayoutParams instead of trying to cast existing ones
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        if (message.isSentByMe()) {
            holder.textSender.setText("You");
            holder.messageBubbleLayout.setBackgroundResource(R.drawable.bubble_sent);
            layoutParams.gravity = Gravity.END;
            holder.textSender.setTextColor(0xFFFFFFFF);
            holder.textContent.setTextColor(0xFFFFFFFF);
            holder.textTimestamp.setTextColor(0xFFC0C0C0);
        } else {
            holder.textSender.setText(message.getTitle());
            holder.messageBubbleLayout.setBackgroundResource(R.drawable.bubble_received);
            layoutParams.gravity = Gravity.START;
            holder.textSender.setTextColor(0xFF3E206D);
            holder.textContent.setTextColor(0xFF333333);
            holder.textTimestamp.setTextColor(0xFF888888);
        }

        holder.messageBubbleLayout.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}