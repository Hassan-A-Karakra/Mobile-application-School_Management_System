package com.example.schoolmanagementsystem;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    // تشفير كلمة المرور
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // التحقق من كلمة المرور المدخلة مقابل الكلمة المشفرة
    public static boolean checkPassword(String enteredPassword, String storedHashedPassword) {
        return BCrypt.checkpw(enteredPassword, storedHashedPassword);
    }
}
