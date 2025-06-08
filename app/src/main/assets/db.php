<?php
$servername = "10.0.2.2";
$username = "root";
$password = "";
$dbname = "school_management";

try {
    // إنشاء الاتصال باستخدام PDO
    $dsn = "mysql:host=" . $servername . ";dbname=" . $dbname . ";charset=utf8mb4";
    $conn = new PDO($dsn, $username, $password);

    // تعيين وضع الخطأ إلى استثناءات
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

} catch (PDOException $e) {
    // في حال فشل الاتصال
    die(json_encode([
        'status' => 'error',
        'message' => 'Connection failed: ' . $e->getMessage()
    ]));
}
?> 