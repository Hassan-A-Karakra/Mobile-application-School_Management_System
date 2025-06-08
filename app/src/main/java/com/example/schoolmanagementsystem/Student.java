package com.example.schoolmanagementsystem;

public class Student {
    private int id;
    private String name;
    private String grade;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
        this.grade = ""; // Initialize with empty string
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}