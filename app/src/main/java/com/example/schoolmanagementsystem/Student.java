package com.example.schoolmanagementsystem;

public class Student {
    private int id;
    private String name;
    private String email;
    private int age;
    private String grade;
    private String currentSubjectScore;
    private boolean isPresent;
    private int absenceCount;

     private String final_score;
    private String total_score;

    
    public Student(int id, String name, String email, int age, String currentSubjectScore, boolean isSubjectScore) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.currentSubjectScore = currentSubjectScore;
        this.grade = "";
         this.final_score = "";
        this.total_score = "";
        this.isPresent = true;
        this.absenceCount = 0;
    }

     public Student(int id, String name, String email, int age, String grade) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.grade = grade;
        this.currentSubjectScore = "";
         this.final_score = "";
        this.total_score = "";
        this.isPresent = true;
        this.absenceCount = 0;
    }

     public Student(int id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.grade = "";
        this.currentSubjectScore = "";
         this.final_score = "";
        this.total_score = "";
        this.isPresent = true;
        this.absenceCount = 0;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getGrade() {
        return grade;
    }

    // New getter and setter for the current subject score
    public String getScore() {
        return currentSubjectScore;
    }

    public void setScore(String currentSubjectScore) {
        this.currentSubjectScore = currentSubjectScore;
    }


    public String getFinal_score() {
        return final_score;
    }

    public String getTotal_score() {
        return total_score;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    // Setters
    public void setPresent(boolean present) {
        isPresent = present;
    }

    public void setAbsenceCount(int count) {
        absenceCount = count;
    }

    public void incrementAbsenceCount() {
        absenceCount++;
    }
}