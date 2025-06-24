<?php
require_once 'db.php';
header('Content-Type: application/json');

$raw_post_data = file_get_contents('php://input');
$data = json_decode($raw_post_data, true);

error_log("Raw POST Data: " . $raw_post_data);
if (json_last_error() !== JSON_ERROR_NONE) {
    error_log("JSON Decode Error: " . json_last_error_msg());
}
error_log("Decoded POST Data: " . print_r($data, true));

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

$conn->begin_transaction();

try {
    $stmt = $conn->prepare("INSERT INTO messages (title, message, student_id) VALUES (?, ?, ?)");
    
    foreach ($student_ids as $student_id) {
        $int_student_id = (int)$student_id;
        $stmt->bind_param("ssi", $title, $message, $int_student_id);
        if (!$stmt->execute()) {
            throw new Exception("Failed to send message to student ID: " . $int_student_id . " - " . $stmt->error);
        }
    }
    
    $conn->commit();
    echo json_encode([
        'status' => 'success',
        'message' => 'Messages sent successfully'
    ]);
} catch (Exception $e) {
    $conn->rollback();
    error_log("Message sending error: " . $e->getMessage());
    echo json_encode([
        'status' => 'error',
        'message' => $e->getMessage()
    ]);
}

$stmt->close();
$conn->close();
?>
