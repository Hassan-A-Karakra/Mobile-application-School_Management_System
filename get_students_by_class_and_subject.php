<?php
require_once 'db.php';
header('Content-Type: application/json');

// التحقق من وجود المعلمات المطلوبة
if (!isset($_GET['class_name']) || !isset($_GET['subject'])) {
    echo json_encode([
        'success' => false,
        'message' => 'الرجاء تحديد الصف والمادة'
    ]);
    exit;
}

$class_name = $_GET['class_name'];
$subject = $_GET['subject'];

// جلب الطلاب في الصف المحدد والذين يدرسون المادة المحددة
$query = "SELECT s.id, s.name, s.email, s.age, g.score 
          FROM students s 
          INNER JOIN student_subjects ss ON s.id = ss.student_id 
          LEFT JOIN grades g ON s.id = g.student_id AND g.subject = ?
          WHERE s.grade = ? AND ss.subject = ?
          ORDER BY s.name ASC";

$stmt = $conn->prepare($query);
$stmt->bind_param("sss", $subject, $class_name, $subject);
$stmt->execute();
$result = $stmt->get_result();

$students = [];
while ($row = $result->fetch_assoc()) {
    $students[] = [
        'id' => $row['id'],
        'name' => $row['name'],
        'email' => $row['email'],
        'age' => $row['age'],
        'score' => $row['score'] ?? ''
    ];
}

if (count($students) > 0) {
    echo json_encode([
        'success' => true,
        'students' => $students
    ]);
} else {
    echo json_encode([
        'success' => false,
        'message' => 'لا يوجد طلاب في هذا الصف يدرسون هذه المادة'
    ]);
}

$stmt->close();
$conn->close();
?> 