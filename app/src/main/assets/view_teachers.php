<?php
require_once 'db.php';

// تنفيذ الاستعلام
$result = $conn->query("SELECT id, name, email, subject FROM teachers");

if (!$result) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Query failed: ' . $conn->error
    ]);
    exit;
}

// جلب البيانات
$teachers = [];
while ($row = $result->fetch_assoc()) {
    $teachers[] = [
        'id' => $row['id'],
        'name' => $row['name'],
        'email' => $row['email'],
        'subject' => $row['subject']
    ];
}

// إرجاع البيانات
echo json_encode([
    'status' => 'success',
    'teachers' => $teachers
]);

// إغلاق الاتصال
$conn->close();
?> 