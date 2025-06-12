<?php
header('Content-Type: application/json');
require 'db.php';

$subjects = [];
$stmt = $conn->prepare("SELECT DISTINCT subject FROM student_subjects ORDER BY subject");

if ($stmt === false) {
    echo json_encode(["status" => "error", "message" => "Failed to prepare statement: " . $conn->error]);
    exit();
}

if ($stmt->execute()) {
    $result = $stmt->get_result();
    while ($row = $result->fetch_assoc()) {
        if (!empty($row['subject'])) {
            $subjects[] = $row['subject'];
        }
    }
    echo json_encode([
        "status" => "success",
        "subjects" => $subjects
    ], JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to fetch subjects: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?> 