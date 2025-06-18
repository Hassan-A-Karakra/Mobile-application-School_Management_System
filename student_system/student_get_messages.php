<?php
require_once 'db.php';
header('Content-Type: application/json');

$data = json_decode(file_get_contents('php://input'), true);
$student_id = isset($data['student_id']) ? (int)$data['student_id'] : 0;

if ($student_id <= 0) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid student ID'
    ]);
    exit;
}

$stmt = $conn->prepare("
    SELECT m.id, m.title, m.message, m.created_at 
    FROM messages m 
    WHERE m.student_id = ? 
    ORDER BY m.created_at DESC
");
$stmt->bind_param("i", $student_id);
$stmt->execute();
$result = $stmt->get_result();

$messages = [];
while ($row = $result->fetch_assoc()) {
    $messages[] = [
        'id' => $row['id'],
        'title' => $row['title'],
        'message' => $row['message'],
        'created_at' => $row['created_at']
    ];
}

echo json_encode([
    'status' => 'success',
    'messages' => $messages
]);

$stmt->close();
$conn->close();
?>
