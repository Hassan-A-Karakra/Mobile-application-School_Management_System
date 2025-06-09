package com.example.schoolmanagementsystem;

public class Student {
    private int id;
    private String name;
    private String email;
    private int grade;
    private int age;
    private boolean present;
    private int absenceCount;

    public Student() {
        this.present = false;
        this.absenceCount = 0;
    }

    public Student(int id, String name) {
    }

    public Student(int id, String name, String email, int age) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public int getAbsenceCount() {
        return absenceCount;
    }

    public void setAbsenceCount(int absenceCount) {
        this.absenceCount = absenceCount;
    }
}