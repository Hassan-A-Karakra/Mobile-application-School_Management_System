<?php
require_once 'db.php';

header('Content-Type: application/json');

$data = json_decode(file_get_contents('php://input'), true);

if (!isset($data['email']) || !isset($data['password'])) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Missing email or password'
    ]);
    exit;
}

$email = $data['email'];
$password = $data['password'];

$stmt = $conn->prepare("SELECT id, name, email, subject FROM teachers WHERE email = ? AND password = ?");
$stmt->bind_param("ss", $email, $password);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $teacher = $result->fetch_assoc();
    echo json_encode([
        'status' => 'success',
        'message' => 'Login successful',
        'teacher' => $teacher
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
