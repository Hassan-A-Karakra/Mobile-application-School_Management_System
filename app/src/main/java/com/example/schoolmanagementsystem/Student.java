package com.example.schoolmanagementsystem;

public class Student {
    private String name;
    private boolean present;
    private String grade;

    private int id;
    private String email;
    private int age;

    public Student(String name, boolean present, String grade) {
        this.name = name;
        this.present = present;
        this.grade = grade;
    }

    public Student(int id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() { return name; }
    public boolean isPresent() { return present; }
    public String getGrade() { return grade; }

    public void setPresent(boolean present) { this.present = present; }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public int getAge() { return age; }
}
