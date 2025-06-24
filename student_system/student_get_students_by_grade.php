<?php
require_once 'db.php';
header('Content-Type: application/json; charset=utf-8');

$grade = $_GET['grade'] ?? '';

if (empty($grade)) {
    echo json_encode(['error' => 'Grade is required']);
    exit;
}

$stmt = $conn->prepare("SELECT id, name FROM students WHERE grade = ? ORDER BY name ASC");
$stmt->bind_param("s", $grade);
$stmt->execute();
$result = $stmt->get_result();

$students = [];
while ($row = $result->fetch_assoc()) {
    $students[] = $row;
}

echo json_encode($students, JSON_UNESCAPED_UNICODE);
$stmt->close();
$conn->close();
?>
