package com.example.schoolmanagementsystem;

import java.sql.*;

public class DatabaseUtil {

    // اتصال قاعدة البيانات
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/school_management";  // تحديث مع تفاصيل الاتصال الخاصة بك
        String username = "root";  // اسم المستخدم الخاص بك
        String password = "";  // كلمة المرور الخاصة بك
        return DriverManager.getConnection(url, username, password);
    }

    // إضافة معلم إلى قاعدة البيانات
    public static void addTeacher(String name, String email, String password, String subject) throws SQLException {
        String query = "INSERT INTO teachers (name, email, password, subject, created_at) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);  // تخزين كلمة المرور بدون تشفير
            stmt.setString(4, subject);
            stmt.executeUpdate();
        }
    }

    // التحقق من كلمة المرور عند تسجيل الدخول
    public static boolean validateLogin(String email, String enteredPassword) throws SQLException {
        String query = "SELECT password FROM teachers WHERE email = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // مقارنة كلمة المرور المدخلة مع كلمة المرور المخزنة في قاعدة البيانات
                return enteredPassword.equals(storedPassword);  // بدون تشفير
            }
        }

        return false;  // إذا لم يتم العثور على البريد الإلكتروني
    }
}
