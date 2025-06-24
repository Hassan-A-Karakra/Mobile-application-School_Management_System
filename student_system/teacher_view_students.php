<?php
require_once 'db.php';
header('Content-Type: application/json');

$grade = isset($_GET['grade']) ? $_GET['grade'] : '';
$subject = isset($_GET['subject']) ? $_GET['subject'] : '';

if (empty($grade) || empty($subject)) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Missing grade or subject'
    ]);
    exit;
}

$query = "SELECT s.id, s.name, s.email, s.grade, s.age
          FROM students s
          JOIN student_subjects ss ON s.id = ss.student_id
          WHERE s.grade = ? AND ss.subject = ?
          ORDER BY s.name ASC";

$stmt = $conn->prepare($query);
if ($stmt === false) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Prepare failed: ' . $conn->error
    ]);
    exit;
}
$stmt->bind_param("ss", $grade, $subject);
$stmt->execute();
$result = $stmt->get_result();

$students = [];
if ($result) {
    while ($row = $result->fetch_assoc()) {
        $students[] = $row;
    }
}

echo json_encode([
    'status' => 'success',
    'students' => $students
]);

$stmt->close();
$conn->close();
?>
