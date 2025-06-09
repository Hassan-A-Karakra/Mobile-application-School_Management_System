package com.example.schoolmanagementsystem;

public class StudentGradeItem {
    private String subject;
    private String grade;
    private String teacher;

    public StudentGradeItem(String subject, String grade, String teacher) {
        this.subject = subject;
        this.grade = grade;
        this.teacher = teacher;
    }

    public String getSubject() { return subject; }
    public String getGrade() { return grade; }
    public String getTeacher() { return teacher; }
}