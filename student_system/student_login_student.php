<?php
require_once 'db.php';

error_log("Login attempt received");

header('Content-Type: application/json');

$data = json_decode(file_get_contents('php://input'), true);

error_log("Received data: " . print_r($data, true));

if (!isset($data['email']) || !isset($data['password'])) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Missing email or password'
    ]);
    exit;
}

$email = $data['email'];
$password = $data['password'];

error_log("Attempting login with email: $email");

$stmt = $conn->prepare("SELECT id, name, email, grade, age FROM students WHERE email = ? AND password = ?");
$stmt->bind_param("ss", $email, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $student = $result->fetch_assoc();
    echo json_encode([
        'status' => 'success',
        'message' => 'Login successful',
        'student' => $student
    ]);
} else {
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid email or password'
    ]);
}

$stmt->close();
$conn->close();
?>
