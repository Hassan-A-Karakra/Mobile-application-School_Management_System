<?php

require_once 'db.php';

header('Content-Type: application/json');

$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';

if (empty($email) || empty($password)) {
    echo json_encode(["status" => "error", "message" => "البريد الإلكتروني وكلمة المرور مطلوبان."], JSON_UNESCAPED_UNICODE);
    exit;
}

$stmt = $conn->prepare("SELECT id, name, email, subject, grade FROM teachers WHERE email = ? AND password = ?");

if ($stmt === false) {
    echo json_encode(["status" => "error", "message" => "خطأ في إعداد الاستعلام: " . $conn->error], JSON_UNESCAPED_UNICODE);
    exit;
}

$stmt->bind_param("ss", $email, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $teacher = $result->fetch_assoc();
    echo json_encode([
        "status" => "success",
        "message" => "تم تسجيل الدخول بنجاح!",
        "teacher_id" => $teacher['id'],
        "teacher_name" => $teacher['name'],
        "teacher_email" => $teacher['email'],
        "teacher_subject" => $teacher['subject'],
        "teacher_grade" => $teacher['grade']
    ], JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode(["status" => "error", "message" => "البريد الإلكتروني أو كلمة المرور غير صحيحة."], JSON_UNESCAPED_UNICODE);
}

$stmt->close();
$conn->close();
?> 