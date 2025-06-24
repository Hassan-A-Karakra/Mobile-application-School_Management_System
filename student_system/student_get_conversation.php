<?php
header('Content-Type: application/json');
$studentId = $_GET['student_id'] ?? '';
$teacherId = $_GET['teacher_id'] ?? '';
$conn = new mysqli('localhost', 'root', '', 'school_management');

$sql = "SELECT sender_type AS sender, content, timestamp 
        FROM messages 
        WHERE student_id = ? AND teacher_id = ?
        ORDER BY timestamp ASC";
$stmt = $conn->prepare($sql);
$stmt->bind_param("ii", $studentId, $teacherId);
$stmt->execute();
$result = $stmt->get_result();

$data = [];
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}
echo json_encode($data);
?>
