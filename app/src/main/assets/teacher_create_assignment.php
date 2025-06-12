<?php
header('Content-Type: application/json');
require 'db.php';

// Get the raw POST data
$input = file_get_contents('php://input');
$data = json_decode($input, true);

if (json_last_error() !== JSON_ERROR_NONE) {
    echo json_encode(["status" => "error", "message" => "Invalid JSON input."]);
    exit();
}

// Extract data
$title = $data['title'] ?? '';
$description = $data['description'] ?? '';
$dueDate = $data['due_date'] ?? '';
$class = $data['class'] ?? '';
$subject = $data['subject'] ?? '';
$grade = $data['grade'] ?? '';

// Basic validation
if (empty($title) || empty($description) || empty($dueDate) || empty($class) || empty($subject) || empty($grade)) {
    echo json_encode(["status" => "error", "message" => "All fields are required."]);
    exit();
}

// Prepare SQL statement
$stmt = $conn->prepare("INSERT INTO assignments (title, description, due_date, class, subject, grade) VALUES (?, ?, ?, ?, ?, ?)");

if ($stmt === false) {
    echo json_encode(["status" => "error", "message" => "Failed to prepare statement: " . $conn->error]);
    exit();
}

// Bind parameters
$stmt->bind_param("ssssss", $title, $description, $dueDate, $class, $subject, $grade);

if ($stmt->execute()) {
    echo json_encode([
        "status" => "success", 
        "message" => "Assignment created successfully!",
        "assignment_id" => $conn->insert_id
    ]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to create assignment: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?> 