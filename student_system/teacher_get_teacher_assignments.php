<?php
header('Content-Type: application/json');
require 'db.php';

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if (json_last_error() !== JSON_ERROR_NONE) {
    echo json_encode(["status" => "error", "message" => "Invalid JSON input."]);
    exit();
}

$teacherGrade = $data['grade'] ?? '';

if (empty($teacherGrade)) {
    echo json_encode(["status" => "error", "message" => "Grade is required for fetching assignments."]);
    exit();
}

$assignments = [];
$stmt = $conn->prepare("SELECT id, title, description, due_date, subject, grade, file_name, file_content FROM assignments WHERE grade = ? ORDER BY due_date DESC");

if ($stmt === false) {
    echo json_encode(["status" => "error", "message" => "Failed to prepare statement: " . $conn->error]);
    exit();
}

$stmt->bind_param("s", $teacherGrade);

if ($stmt->execute()) {
    $result = $stmt->get_result();
    while ($row = $result->fetch_assoc()) {
        $assignment = [
            "id" => $row['id'],
            "title" => $row['title'],
            "description" => $row['description'],
            "due_date" => $row['due_date'],
            "subject" => $row['subject'],
            "grade" => $row['grade'],
            "file_name" => $row['file_name'],
        ];
        $assignments[] = $assignment;
    }
    echo json_encode([
        "status" => "success",
        "assignments" => $assignments
    ]);
} else {
    echo json_encode(["status" => "error", "message" => "Failed to fetch assignments: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>
