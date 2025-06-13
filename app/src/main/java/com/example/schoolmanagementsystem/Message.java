package com.example.schoolmanagementsystem;

public class Message {
    private final String title; // This will hold the sender's name or "Message from Student X"
    private final String messageContent;
    private final String timestamp;
    private boolean isSentByMe; // True if sent by the logged-in student

    public Message(String title, String messageContent, String timestamp, boolean isSentByMe) {
        this.title = title;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.isSentByMe = isSentByMe;
    }

    public String getTitle() {
        return title;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isSentByMe() {
        return isSentByMe;
    }
}