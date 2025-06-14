package com.example.schoolmanagementsystem;

public class Assignment {
    private int id;
    private String title;
    private String description;
    private String dueDate;
    private String subject;
    private String grade;
    private String fileName; // New field for file name
    private String filePath; // New field for file path (or URL to download)
    private boolean submitted;

    // Constructor with all fields including new file details
    public Assignment(int id, String title, String description, String dueDate, String subject, String grade, String fileName, String filePath) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.subject = subject;
        this.grade = grade;
        this.fileName = fileName;
        this.filePath = filePath;
        this.submitted = false;
    }

    // Constructor without file details (if still needed)
    public Assignment(int id, String title, String description, String dueDate, String subject, String grade) {
        this(id, title, description, dueDate, subject, grade, null, null); // Call the main constructor with null for file fields
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getSubject() {
        return subject;
    }

    public String getGrade() {
        return grade;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    // You can add setters if you need to modify these properties after creation
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }
}