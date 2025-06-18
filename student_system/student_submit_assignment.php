<?php
require_once 'db.php';

header('Content-Type: application/json');

$data = json_decode(file_get_contents('php://input'), true);
$student_id = isset($data['student_id']) ? intval($data['student_id']) : null;
$assignment_id = isset($data['assignment_id']) ? intval($data['assignment_id']) : null;
$content = isset($data['content']) ? trim($data['content']) : null;

if (!$student_id || $student_id <= 0) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Invalid student ID']);
    exit;
}

if (!$assignment_id || $assignment_id <= 0) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Invalid assignment ID']);
    exit;
}

if (!$content || strlen($content) === 0) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Content cannot be empty']);
    exit;
}

$sql_check_student = "SELECT grade FROM students WHERE id = ?";
$stmt_check_student = $conn->prepare($sql_check_student);
if (!$stmt_check_student) {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => 'Failed to prepare student check statement: ' . $conn->error]);
    exit;
}

$stmt_check_student->bind_param("i", $student_id);
$stmt_check_student->execute();
$student_result = $stmt_check_student->get_result();
$student = $student_result->fetch_assoc();

if (!$student) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Student not found']);
    exit;
}

$sql_check_assignment = "SELECT due_date FROM assignments WHERE id = ? AND grade = ?";
$stmt_check_assignment = $conn->prepare($sql_check_assignment);
if (!$stmt_check_assignment) {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => 'Failed to prepare assignment check statement: ' . $conn->error]);
    exit;
}

$stmt_check_assignment->bind_param("is", $assignment_id, $student['grade']);
$stmt_check_assignment->execute();
$result = $stmt_check_assignment->get_result();
$assignment = $result->fetch_assoc();

if (!$assignment) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Assignment not found or not assigned to your grade']);
    exit;
}

$sql_check = "SELECT id FROM submissions WHERE student_id = ? AND assignment_id = ?";
$stmt_check = $conn->prepare($sql_check);
if (!$stmt_check) {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => 'Failed to prepare submission check statement: ' . $conn->error]);
    exit;
}

$stmt_check->bind_param("ii", $student_id, $assignment_id);
$stmt_check->execute();
$stmt_check->store_result();

if ($stmt_check->num_rows > 0) {
    $sql = "UPDATE submissions SET content = ?, submitted_at = NOW() WHERE student_id = ? AND assignment_id = ?";
    $stmt = $conn->prepare($sql);
    if (!$stmt) {
        http_response_code(500);
        echo json_encode(['success' => false, 'message' => 'Failed to prepare update statement: ' . $conn->error]);
        exit;
    }
    $stmt->bind_param("sii", $content, $student_id, $assignment_id);
} else {
    $sql = "INSERT INTO submissions (student_id, assignment_id, content, submitted_at) VALUES (?, ?, ?, NOW())";
    $stmt = $conn->prepare($sql);
    if (!$stmt) {
        http_response_code(500);
        echo json_encode(['success' => false, 'message' => 'Failed to prepare insert statement: ' . $conn->error]);
        exit;
    }
    $stmt->bind_param("iis", $student_id, $assignment_id, $content);
}

if ($stmt->execute()) {
    echo json_encode(['success' => true, 'message' => 'Submission saved successfully']);
} else {
    http_response_code(500);
    echo json_encode(['success' => false, 'message' => 'Failed to save submission: ' . $stmt->error]);
}

$stmt_check_student->close();
$stmt_check_assignment->close();
$stmt_check->close();
$stmt->close();
$conn->close();
?>
