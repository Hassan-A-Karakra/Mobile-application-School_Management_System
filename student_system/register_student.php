<?php

error_reporting(E_ALL);
ini_set('display_errors', 1);

require_once 'db.php';

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode([
        'status' => 'error',
        'message' => 'Only POST method is allowed'
    ]);
    exit;
}

$data = $_POST;

if (!isset($data['name'], $data['email'], $data['password'], $data['age'], $data['grade'], $data['subject'])) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Missing required fields or subject'
    ]);
    exit;
}

$name = trim($conn->real_escape_string($data['name']));
$email = trim($conn->real_escape_string($data['email']));
$password = trim($conn->real_escape_string($data['password']));
$age = (int)$data['age'];
$grade = trim($conn->real_escape_string($data['grade']));
$subject = trim($conn->real_escape_string($data['subject']));

if (empty($name) || empty($email) || empty($password) || empty($age) || empty($grade) || empty($subject)) {
    echo json_encode([
        'status' => 'error',
        'message' => 'All fields are required'
    ]);
    exit;
}

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid email format'
    ]);
    exit;
}

$conn->begin_transaction();

try {
    $stmt = $conn->prepare("INSERT INTO students (name, email, password, age, grade) VALUES (?, ?, ?, ?, ?)");
    $stmt->bind_param("sssis", $name, $email, $password, $age, $grade);

    if (!$stmt->execute()) {
        throw new Exception('Student registration failed: ' . $stmt->error);
    }
    $student_id = $conn->insert_id;
    $stmt->close();

    $stmt_subject = $conn->prepare("INSERT INTO student_subjects (student_id, subject) VALUES (?, ?)");
    if (!$stmt_subject) {
        throw new Exception('Failed to prepare subject insertion: ' . $conn->error);
    }
    $stmt_subject->bind_param("is", $student_id, $subject);

    if (!$stmt_subject->execute()) {
        throw new Exception('Subject assignment failed: ' . $stmt_subject->error);
    }
    $stmt_subject->close();

    $conn->commit();

    echo json_encode([
        'status' => 'success',
        'message' => 'Student registered and subject assigned successfully',
        'student_id' => $student_id
    ]);

} catch (Exception $e) {
    $conn->rollback();
    echo json_encode([
        'status' => 'error',
        'message' => 'Registration failed: ' . $e->getMessage()
    ]);
} finally {
    $conn->close();
}
?>
