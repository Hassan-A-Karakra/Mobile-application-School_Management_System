<?php
require 'db.php';

header('Content-Type: application/json');
$data = json_decode(file_get_contents('php://input'), true);

$student_id = $data['student_id'] ?? null;
$assignment_id = $data['assignment_id'] ?? null;
$content = $data['content'] ?? null;

if (!$student_id || !$assignment_id || !$content) {
    echo json_encode(["status" => "error", "message" => "Missing data. Required: student_id, assignment_id, content."]);
    exit;
}

$sql = "INSERT INTO submissions (student_id, assignment_id, content) VALUES (?, ?, ?)";
$stmt = $conn->prepare($sql);

if ($stmt === false) {
    error_log("Failed to prepare statement for submission: " . $conn->error);
    echo json_encode(["status" => "error", "message" => "Database error: Could not prepare statement."]);
    exit;
}

$stmt->bind_param("iis", $student_id, $assignment_id, $content);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Assignment submitted successfully!"]);
} else {
    error_log("Failed to execute statement for submission: " . $stmt->error);
    echo json_encode(["status" => "error", "message" => "Submission failed: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>
