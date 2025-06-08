<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

require_once 'db.php';

$teacher_id = $_GET['teacher_id'] ?? null;

if (!$teacher_id) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Teacher ID is required'
    ]);
    exit;
}

try {
    // Get schedule for the teacher
    $stmt = $conn->prepare(
        "SELECT s.subject, s.day, s.time, s.grade 
         FROM schedules s
         WHERE s.teacher_id = ?
         ORDER BY FIELD(s.day, 'Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'), s.time"
    );
    
    if (!$stmt) {
        throw new Exception('Prepare failed: ' . $conn->error);
    }
    
    $stmt->bind_param("i", $teacher_id);
    
    if (!$stmt->execute()) {
        throw new Exception('Execute failed: ' . $stmt->error);
    }
    
    $result = $stmt->get_result();
    $schedule = [];
    
    while ($row = $result->fetch_assoc()) {
        $schedule[] = $row;
    }
    
    echo json_encode([
        'status' => 'success',
        'schedule' => $schedule
    ]);

} catch (Exception $e) {
    echo json_encode([
        'status' => 'error',
        'message' => $e->getMessage()
    ]);
} finally {
    if (isset($stmt)) {
        $stmt->close();
    }
    if (isset($conn)) {
        $conn->close();
    }
}
?> 