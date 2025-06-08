package com.example.schoolmanagementsystem;

public class Student {
    private String name;
    private boolean present;
    private String grade;

    private int id;
    private String email;
    private int age;

    // كونستركتور للحضور والتقارير
    public Student(String name, boolean present, String grade) {
        this.name = name;
        this.present = present;
        this.grade = grade;
    }

    // كونستركتور لجلب الطلاب من السيرفر (id, name, email, age)
    public Student(int id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    // كونستركتور شامل لكل الحقول (للاستخدام المرن)
    public Student(String name, boolean present, String grade, int id, String email, int age) {
        this.name = name;
        this.present = present;
        this.grade = grade;
        this.id = id;
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