package com.example.schoolmanagementsystem;

public class StudentGradeItem {
    private String subject;
    private String grade;
    private String absences;
    private String teacherName;

    public StudentGradeItem(String subject, String grade, String absences, String teacherName) {
        this.subject = subject;
        this.grade = grade;
        this.absences = absences;
        this.teacherName = teacherName;
    }

    public String getSubject() {
        return subject;
    }

    public String getGrade() {
        return grade;
    }

    public String getAbsences() {
        return absences;
    }

    public String getTeacherName() {
        return teacherName;
    }
}