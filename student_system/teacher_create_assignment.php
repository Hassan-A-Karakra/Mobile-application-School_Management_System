<?php
header('Content-Type: application/json');
require 'db.php';

$input = file_get_contents('php://input');
$data = json_decode($input, true);

error_log("Received data for assignment creation: " . print_r($data, true));

if (json_last_error() !== JSON_ERROR_NONE) {
    echo json_encode(["status" => "error", "message" => "Invalid JSON input."]);
    exit();
}

$title = $data['title'] ?? '';
$description = $data['description'] ?? '';
$dueDate = $data['due_date'] ?? '';
$subject = $data['subject'] ?? '';
$grade = $data['grade'] ?? '';
$fileName = $data['file_name'] ?? null;
$fileContentBase64 = $data['file_content'] ?? null;

$fileContent = null;
if ($fileContentBase64 !== null) {
    $fileContent = base64_decode($fileContentBase64);
    if ($fileContent === false) {
        echo json_encode(["status" => "error", "message" => "Failed to decode file content."]);
        exit();
    }
}

if (empty($title) || empty($description) || empty($dueDate) || empty($subject) || empty($grade)) {
    echo json_encode(["status" => "error", "message" => "All fields (title, description, due date, subject, grade) are required."]);
    exit();
}

$stmt = $conn->prepare("INSERT INTO assignments (title, description, due_date, subject, grade, file_name, file_content) VALUES (?, ?, ?, ?, ?, ?, ?)");

if ($stmt === false) {
    echo json_encode(["status" => "error", "message" => "Failed to prepare statement: " . $conn->error]);
    exit();
}

$stmt->bind_param("ssssssb", $title, $description, $dueDate, $subject, $grade, $fileName, $fileContent);

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
