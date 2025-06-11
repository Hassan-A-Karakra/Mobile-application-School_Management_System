<?php
require_once 'db.php';
header('Content-Type: application/json');

if (!isset($_GET['grade'])) {
    echo json_encode([]);
    exit;
}

$grade = $conn->real_escape_string($_GET['grade']);
$subjects = [];
$stmt = $conn->prepare("SELECT DISTINCT ss.subject 
                       FROM student_subjects ss 
                       JOIN students s ON ss.student_id = s.id 
                       WHERE s.grade = ? 
                       ORDER BY ss.subject");

if ($stmt === false) {
    echo json_encode(["status" => "error", "message" => "Failed to prepare statement: " . $conn->error]);
    exit();
}

$stmt->bind_param("s", $grade);

if ($stmt->execute()) {
    $result = $stmt->get_result();
    while ($row = $result->fetch_assoc()) {
        if (!empty($row['subject'])) {
            $subjects[] = $row['subject'];
        }
    }
    echo json_encode($subjects);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to fetch subjects: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?> 