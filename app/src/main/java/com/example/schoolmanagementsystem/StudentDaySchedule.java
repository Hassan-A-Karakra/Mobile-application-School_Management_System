package com.example.schoolmanagementsystem;

import java.util.List;

public class StudentDaySchedule {
    public String day;
    public List<StudentClassSession> sessions;
    public StudentDaySchedule(String day, List<StudentClassSession> sessions) {
        this.day = day;
        this.sessions = sessions;
    }
}