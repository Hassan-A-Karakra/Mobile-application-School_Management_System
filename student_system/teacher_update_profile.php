<?php
error_reporting(0);
ini_set('display_errors', 0);
include 'db.php';

header('Content-Type: application/json');

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $teacherId = $_POST['teacher_id'] ?? '';
    $name = $_POST['name'] ?? '';
    $email = $_POST['email'] ?? '';
    $subject = $_POST['subject'] ?? '';

    if (empty($teacherId) || empty($name) || empty($email) || empty($subject)) {
        echo json_encode(['status' => 'error', 'message' => 'Name, email, and subject are required for update.']);
        exit();
    }

    $sql = "UPDATE teachers SET name = ?, email = ?, subject = ? WHERE id = ?";
    $stmt = $conn->prepare($sql);

    if ($stmt === false) {
        echo json_encode(['status' => 'error', 'message' => 'Prepare failed: ' . $conn->error]);
        exit();
    }

    $stmt->bind_param("sssi", $name, $email, $subject, $teacherId);

    if ($stmt->execute()) {
        if ($stmt->affected_rows > 0) {
            echo json_encode(['status' => 'success', 'message' => 'Profile updated successfully.']);
        } else {
            echo json_encode(['status' => 'error', 'message' => 'No changes made to profile or teacher not found.']);
        }
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Execute failed: ' . $stmt->error]);
    }

    $stmt->close();
    $conn->close();
} else {
    echo json_encode(['status' => 'error', 'message' => 'Invalid request method.']);
}
?>
