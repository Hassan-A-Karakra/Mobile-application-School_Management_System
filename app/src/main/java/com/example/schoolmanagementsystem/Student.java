package com.example.schoolmanagementsystem;


public class Student {
    private String name;
    private boolean isPresent;  // حالة الحضور
    private String grade;  // الدرجة

    // Constructor
    public Student(String name, boolean isPresent, String grade) {
        this.name = name;
        this.isPresent = isPresent;
        this.grade = grade;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public String getGrade() {
        return grade;
    }

    // Setter methods (إذا كانت مطلوبة)
    public void setName(String name) {
        this.name = name;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
