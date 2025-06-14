<?php
header('Content-Type: application/json'); // Ensure JSON header is sent
require_once 'db.php';

// Decode input
$data = json_decode(file_get_contents("php://input"), true);
$student_id = isset($data['student_id']) ? intval($data['student_id']) : null;

if (!$student_id || $student_id <= 0) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Invalid student ID']);
    exit;
}

// Get student's grade
$grade_stmt = $conn->prepare("SELECT grade FROM students WHERE id = ?");
$grade_stmt->bind_param("i", $student_id);
$grade_stmt->execute();
$grade_result = $grade_stmt->get_result();
$student = $grade_result->fetch_assoc();
$grade_stmt->close();

if (!$student) {
    http_response_code(404);
    echo json_encode(['status' => 'error', 'message' => 'Student not found']);
    exit;
}

// Fetch assignments for student's grade
$sql = "SELECT a.id, a.title, a.description, a.due_date, a.grade, a.subject, s.submitted_at
        FROM assignments a
        LEFT JOIN submissions s ON a.id = s.assignment_id AND s.student_id = ?
        WHERE a.grade = ?
        ORDER BY a.due_date ASC";

$stmt = $conn->prepare($sql);
if (!$stmt) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Prepare failed: ' . $conn->error]);
    exit;
}

$stmt->bind_param("is", $student_id, $student['grade']);
if (!$stmt->execute()) {
    http_response_code(500);
    echo json_encode(['status' => 'error', 'message' => 'Execute failed: ' . $stmt->error]);
    exit;
}

$result = $stmt->get_result();
$assignments = [];
while ($row = $result->fetch_assoc()) {
    // Note: due_date and submitted_at are already in appropriate format from DB or can be formatted client-side.
    // If you need specific formatting for PHP's date function, ensure the column type is DATE/DATETIME.
    // For simplicity, removing the re-formatting unless explicitly needed.
    // if (isset($row['due_date'])) {
    //     $row['due_date'] = date('Y-m-d H:i:s', strtotime($row['due_date']));
    // }
    // if (isset($row['submitted_at'])) {
    //     $row['submitted_at'] = date('Y-m-d H:i:s', strtotime($row['submitted_at']));
    // }
    $assignments[] = $row;
}

echo json_encode(['status' => 'success', 'assignments' => $assignments]);
exit; // Exit after JSON output

$stmt->close();
$conn->close();
?> 