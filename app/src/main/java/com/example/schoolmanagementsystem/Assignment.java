package com.example.schoolmanagementsystem;

public class Assignment {
    private int id; // Assignment ID
    private String title; // Title of the assignment
    private String description; // Description of the assignment
    private String dueDate; // Due date of the assignment
    private String assignmentClass; // Class info (optional)
    private String assignmentSubject; // Subject info (optional)
    private String grade; // Grade (nullable)

    // Full constructor
    public Assignment(int id, String title, String description, String dueDate, String assignmentClass, String assignmentSubject, String grade) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.assignmentClass = assignmentClass;
        this.assignmentSubject = assignmentSubject;
        this.grade = grade;
    }
    public Assignment(int id, String title, String description, String dueDate, String assignmentClass, String assignmentSubject) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.assignmentClass = assignmentClass;
        this.assignmentSubject = assignmentSubject;
        this.grade = "Not graded yet"; // Default value
    }


    // Constructor without class/subject/grade
    public Assignment(int id, String title, String description, String dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.assignmentClass = "";
        this.assignmentSubject = "";
        this.grade = "Not graded yet";
    }

    // Empty constructor (for parsing flexibility)
    public Assignment() {}

    // Getter methods
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

    public String getGrade() {
        return grade;
    }

    // Setter methods
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

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
