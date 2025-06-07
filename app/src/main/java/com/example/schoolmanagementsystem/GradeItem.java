package com.example.schoolmanagementsystem;

public class GradeItem {
    private String subject;
    private String grade;
    private String teacher;

    public GradeItem(String subject, String grade, String teacher) {
        this.subject = subject;
        this.grade = grade;
        this.teacher = teacher;
    }

    public String getSubject() { return subject; }
    public String getGrade() { return grade; }
    public String getTeacher() { return teacher; }
}