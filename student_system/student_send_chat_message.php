<?php
require_once 'db.php';
header('Content-Type: application/json');

$raw_post_data = file_get_contents('php://input');
$data = json_decode($raw_post_data, true);

error_log("send_chat_message.php - Raw POST Data: " . $raw_post_data);
if (json_last_error() !== JSON_ERROR_NONE) {
    error_log("send_chat_message.php - JSON Decode Error: " . json_last_error_msg());
}
error_log("send_chat_message.php - Decoded POST Data: " . print_r($data, true));

if (!isset($data['sender_id']) ||
    !isset($data['sender_type']) ||
    !isset($data['receiver_id']) ||
    !isset($data['receiver_type']) ||
    !isset($data['message_content'])) {
    
    echo json_encode([
        'status' => 'error',
        'message' => 'Missing required fields (sender_id, sender_type, receiver_id, receiver_type, message_content)'
    ]);
    exit;
}

$sender_id = (int)$data['sender_id'];
$sender_type = $conn->real_escape_string($data['sender_type']);
$receiver_id = (int)$data['receiver_id'];
$receiver_type = $conn->real_escape_string($data['receiver_type']);
$message_content = $conn->real_escape_string($data['message_content']);

if (!in_array($sender_type, ['student', 'teacher']) || !in_array($receiver_type, ['student', 'teacher'])) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid sender_type or receiver_type. Must be \'student\' or \'teacher\'.'
    ]);
    exit;
}

$stmt = $conn->prepare("INSERT INTO chat_messages (sender_id, sender_type, receiver_id, receiver_type, message_content) VALUES (?, ?, ?, ?, ?)");

if (!$stmt) {
    error_log("send_chat_message.php - Prepare failed: " . $conn->error);
    echo json_encode([
        'status' => 'error',
        'message' => 'Database error: Failed to prepare statement.'
    ]);
    exit;
}

$stmt->bind_param("isiss", $sender_id, $sender_type, $receiver_id, $receiver_type, $message_content);

if ($stmt->execute()) {
    echo json_encode([
        'status' => 'success',
        'message' => 'Message sent successfully.'
    ]);
} else {
    error_log("send_chat_message.php - Execute failed: " . $stmt->error);
    echo json_encode([
        'status' => 'error',
        'message' => 'Failed to send message: ' . $stmt->error
    ]);
}

$stmt->close();
$conn->close();
?>
