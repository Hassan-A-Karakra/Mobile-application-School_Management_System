<?php
require_once 'db.php';
header('Content-Type: application/json');

// Get POST data
$data = json_decode(file_get_contents('php://input'), true);

if (!isset($data['title']) || !isset($data['message']) || !isset($data['student_ids'])) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Missing required fields'
    ]);
    exit;
}

$title = $conn->real_escape_string($data['title']);
$message = $conn->real_escape_string($data['message']);
$student_ids = $data['student_ids'];

if (empty($student_ids)) {
    echo json_encode([
        'status' => 'error',
        'message' => 'No students selected'
    ]);
    exit;
}

// First, create the messages table if it doesn't exist
$create_table_sql = "CREATE TABLE IF NOT EXISTS messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    student_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
)";

if (!$conn->query($create_table_sql)) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Failed to create messages table: ' . $conn->error
    ]);
    exit;
}

// Begin transaction
$conn->begin_transaction();

try {
    $stmt = $conn->prepare("INSERT INTO messages (title, message, student_id) VALUES (?, ?, ?)");
    
    foreach ($student_ids as $student_id) {
        $stmt->bind_param("ssi", $title, $message, $student_id);
        if (!$stmt->execute()) {
            throw new Exception("Failed to send message to student ID: " . $student_id);
        }
    }
    
    $conn->commit();
    echo json_encode([
        'status' => 'success',
        'message' => 'Messages sent successfully'
    ]);
} catch (Exception $e) {
    $conn->rollback();
    echo json_encode([
        'status' => 'error',
        'message' => $e->getMessage()
    ]);
}

$stmt->close();
$conn->close();
?> 