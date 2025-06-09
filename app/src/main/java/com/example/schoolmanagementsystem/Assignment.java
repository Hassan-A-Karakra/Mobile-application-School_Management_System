package com.example.schoolmanagementsystem;

public class Assignment {
    private int id; // Assignment ID, integer type
    private String title; // Assignment title
    private String description; // Assignment description
    private String dueDate; // Assignment due date
    private String assignmentClass; // Class to which the assignment belongs
    private String assignmentSubject; // Subject to which the assignment belongs

    // Constructor for the Assignment class with all fields
    public Assignment(int id, String title, String description, String dueDate, String assignmentClass, String assignmentSubject) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.assignmentClass = assignmentClass;
        this.assignmentSubject = assignmentSubject;
    }

    // Second constructor for Assignment (e.g., for backward compatibility if class/subject are optional)
    public Assignment(int id, String title, String description, String dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.assignmentClass = "";  // Can be left empty or assigned a default value
        this.assignmentSubject = ""; // Can be left empty or assigned a default value
    }

    // Getter methods to access properties
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

    public String getAssignmentClass() {
        return assignmentClass;
    }

    public String getAssignmentSubject() {
        return assignmentSubject;
    }

    // Setter methods to set properties
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

    public void setAssignmentClass(String assignmentClass) {
        this.assignmentClass = assignmentClass;
    }

    public void setAssignmentSubject(String assignmentSubject) {
        this.assignmentSubject = assignmentSubject;
    }
}