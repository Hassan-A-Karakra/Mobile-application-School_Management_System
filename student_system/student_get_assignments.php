<?php
require_once 'db.php';

$data = json_decode(file_get_contents("php://input"), true);
$student_id = isset($data['student_id']) ? intval($data['student_id']) : null;

if (!$student_id || $student_id <= 0) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Invalid student ID']);
    exit;
}

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
    $assignments[] = $row;
}

echo json_encode(['status' => 'success', 'assignments' => $assignments]);

$stmt->close();
$conn->close();
?>
