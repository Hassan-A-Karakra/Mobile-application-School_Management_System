<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require_once 'db.php';
header('Content-Type: application/json');

$input = file_get_contents('php://input');
$data = json_decode($input, true);

if (!isset($data['subject']) || !isset($data['attendance'])) {
    echo json_encode(['success' => false, 'message' => 'Missing required data: subject or attendance records.']);
    exit;
}

$subject = $conn->real_escape_string($data['subject']);
$attendance_records = $data['attendance'];

$conn->begin_transaction();

try {
    foreach ($attendance_records as $record) {
        $student_id = (int)$record['student_id'];
        $is_present_bool = (bool)$record['is_present'];

        if ($is_present_bool) {
            $stmt = $conn->prepare("INSERT INTO attendance (student_id, subject, absence_count) VALUES (?, ?, 0) ON DUPLICATE KEY UPDATE absence_count = 0");
            $stmt->bind_param("is", $student_id, $subject);
        } else {
            $stmt = $conn->prepare("INSERT INTO attendance (student_id, subject, absence_count) VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE absence_count = absence_count + 1");
            $stmt->bind_param("is", $student_id, $subject);
        }
        
        if ($stmt === false) {
            throw new Exception("Failed to prepare statement: " . $conn->error);
        }

        if (!$stmt->execute()) {
            throw new Exception("Failed to execute statement for student " . $student_id . ": " . $stmt->error);
        }
        $stmt->close();
    }

    $conn->commit();
    echo json_encode(['success' => true, 'message' => 'Attendance saved successfully.']);

} catch (Exception $e) {
    $conn->rollback();
    error_log("Failed to save attendance: " . $e->getMessage());
    echo json_encode(['success' => false, 'message' => 'Failed to save attendance: ' . $e->getMessage()]);
} finally {
    if (isset($conn)) {
        $conn->close();
    }
}
?>
