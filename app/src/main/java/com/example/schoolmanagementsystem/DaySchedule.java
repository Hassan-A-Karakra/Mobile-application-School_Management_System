package com.example.schoolmanagementsystem;

import java.util.List;

public class DaySchedule {
    public String day;
    public List<ClassSession> sessions;
    public DaySchedule(String day, List<ClassSession> sessions) {
        this.day = day;
        this.sessions = sessions;
    }
}