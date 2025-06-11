<?php
require_once 'db.php';
header('Content-Type: application/json');

// Get JSON input
$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Check if required parameters are set
if (!isset($data['subject']) || !isset($data['date']) || !isset($data['attendance'])) {
    echo json_encode(['success' => false, 'message' => 'Missing required data: subject, date, or attendance records.']);
    exit;
}

$subject = $data['subject'];
$date = $data['date']; // التاريخ هنا يمكن استخدامه لتسجيل الحضور اليومي، لكن حالياً يتم تحديث عدد الغيابات الكلي.
$attendance_records = $data['attendance'];

// Start a database transaction for atomicity
$conn->begin_transaction();

try {
    // Loop through each attendance record received from the app
    foreach ($attendance_records as $record) {
        $student_id = $record['student_id'];
        $is_present = $record['is_present']; // true if present, false if absent

        // If the student is marked as absent, update their total absence count
        if ($is_present == false) {
            // This query will:
            // 1. Try to INSERT a new record for the student and subject with absence_count = 1.
            // 2. If a record already exists (due to a UNIQUE INDEX on student_id and subject),
            //    it will instead UPDATE the existing record by incrementing absence_count by 1.
            $stmt = $conn->prepare("INSERT INTO absences (student_id, subject, absence_count) VALUES (?, ?, 1) ON DUPLICATE KEY UPDATE absence_count = absence_count + 1");
            $stmt->bind_param("is", $student_id, $subject); // 'i' for integer (student_id), 's' for string (subject)
            $stmt->execute();
            $stmt->close();
        }
        // If the student is present (is_present == true), we do nothing to their absence_count in the 'absences' table.
        // This table is designed to track total absences, not daily presence.
    }

    // If all updates/inserts are successful, commit the transaction
    $conn->commit();
    echo json_encode(['success' => true, 'message' => 'Attendance saved successfully.']);

} catch (Exception $e) {
    // If any error occurs, rollback the transaction
    $conn->rollback();
    error_log("Failed to save attendance: " . $e->getMessage()); // Log the error for debugging
    echo json_encode(['success' => false, 'message' => 'Failed to save attendance: An error occurred.']);
} finally {
    // Close the database connection
    $conn->close();
}
?> 