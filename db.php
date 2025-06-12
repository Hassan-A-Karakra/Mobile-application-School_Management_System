<?php
$servername = "localhost";
$username = "root"; // عادةً "root" لـ XAMPP
$password = ""; // كلمة مرور قاعدة البيانات (عادةً فارغة لـ XAMPP)
$dbname = "school_system_db"; // اسم قاعدة البيانات الخاصة بك

// إنشاء اتصال بقاعدة البيانات
$conn = new mysqli($servername, $username, $password, $dbname);

// التحقق من الاتصال
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
