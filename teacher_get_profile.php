<?php
error_reporting(0);
ini_set('display_errors', 0);
include 'db.php';

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $teacherId = $_POST['teacher_id'] ?? '';

    if (empty($teacherId)) {
        echo json_encode(['status' => 'error', 'message' => 'Teacher ID is required.']);
        exit();
    }

    $sql = "SELECT id, name, email, subject FROM teachers WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bind_param("i", $teacherId);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $teacher = $result->fetch_assoc();
        echo json_encode(['status' => 'success', 'teacher' => $teacher]);
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Teacher not found.']);
    }

    $stmt->close();
    $conn->close();
} else {
    echo json_encode(['status' => 'error', 'message' => 'Invalid request method.']);
}
?> 