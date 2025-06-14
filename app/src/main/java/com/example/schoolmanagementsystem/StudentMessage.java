package com.example.schoolmanagementsystem;

public class StudentMessage {
    private final String title;
    private final String messageContent;
    private final String timestamp;
    private boolean isSentByMe;

    public StudentMessage(String title, String messageContent, String timestamp, boolean isSentByMe) {
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