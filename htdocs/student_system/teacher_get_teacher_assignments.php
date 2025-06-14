<?php
header('Content-Type: application/json');
require 'db.php';

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if (json_last_error() !== JSON_ERROR_NONE) {
    echo json_encode(["status" => "error", "message" => "Invalid JSON input."]);
    exit();
}

// Expect 'grade' (teacher's grade/class) from POST data
$teacherGrade = $data['grade'] ?? '';

if (empty($teacherGrade)) {
    echo json_encode(["status" => "error", "message" => "Grade is required for fetching assignments."]);
    exit();
}

$assignments = [];
// Select assignments filtered by the teacher's grade
$stmt = $conn->prepare("SELECT id, title, description, due_date, subject, grade FROM assignments WHERE grade = ? ORDER BY due_date DESC");

if ($stmt === false) {
    echo json_encode(["status" => "error", "message" => "Failed to prepare statement: " . $conn->error]);
    exit();
}

$stmt->bind_param("s", $teacherGrade);

if ($stmt->execute()) {
    $result = $stmt->get_result();
    while ($row = $result->fetch_assoc()) {
        $assignments[] = $row;
    }
    echo json_encode(
        [
            "status" => "success", 
            "assignments" => $assignments
        ]
    );
} else {
    echo json_encode(["status" => "error", "message" => "Failed to fetch assignments: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?> 