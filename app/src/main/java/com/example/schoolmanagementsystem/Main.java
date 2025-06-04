package com.example.schoolmanagementsystem;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // تسجيل الدخول أو التسجيل كمعلم
        System.out.println("Welcome! Please select an option:");
        System.out.println("1. Login");
        System.out.println("2. Register");

        int choice = scanner.nextInt();
        scanner.nextLine();  // لتخطي السطر الفارغ

        if (choice == 1) {
            // تسجيل الدخول
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            try {
                if (DatabaseUtil.validateLogin(email, password)) {
                    System.out.println("Login successful!");
                } else {
                    System.out.println("Invalid email or password!");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }

        } else if (choice == 2) {
            // إنشاء حساب معلم جديد
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();
            System.out.print("Enter your subject: ");
            String subject = scanner.nextLine();

            try {
                DatabaseUtil.addTeacher(name, email, password, subject);  // إضافة المعلم إلى قاعدة البيانات
                System.out.println("Registration successful!");
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid choice!");
        }

        scanner.close();
    }
}
