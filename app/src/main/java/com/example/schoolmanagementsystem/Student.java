package com.example.schoolmanagementsystem;

public class Student {
    private int id;
    private String name;
    private String email;
    private int age;
    private String grade; // This is the class grade, e.g., "10", "11", "12"
    private String currentSubjectScore; // This will hold the score for the current subject

    // Existing score fields (if still needed for other parts of the app)
    private String quiz_score;
    private String midterm_score;
    private String final_score;
    private String total_score;

    // Constructor for fetching students with scores (from PHP, where it's called 'score')
    // Added a boolean parameter 'isSubjectScore' to differentiate this constructor
    public Student(int id, String name, String email, int age, String currentSubjectScore, boolean isSubjectScore) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.currentSubjectScore = currentSubjectScore;
        this.grade = ""; // Initialize as it's not always fetched with this constructor
        this.quiz_score = "";
        this.midterm_score = "";
        this.final_score = "";
        this.total_score = "";
    }

    // Original constructor for student list without scores (e.g., in Teacher_view_students)
    public Student(int id, String name, String email, int age, String grade) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.grade = grade;
        this.currentSubjectScore = ""; // Initialize new field
        this.quiz_score = "";
        this.midterm_score = "";
        this.final_score = "";
        this.total_score = "";
    }

    // Original constructor for basic student info
    public Student(int id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.grade = ""; // Initialize new field
        this.currentSubjectScore = ""; // Initialize new field
        this.quiz_score = "";
        this.midterm_score = "";
        this.final_score = "";
        this.total_score = "";
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

    // Getters for other scores (if still used elsewhere)
    public String getQuiz_score() {
        return quiz_score;
    }

    public String getMidterm_score() {
        return midterm_score;
    }

    public String getFinal_score() {
        return final_score;
    }

    public String getTotal_score() {
        return total_score;
    }
}