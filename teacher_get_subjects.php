<?php
require_once 'db.php';
header('Content-Type: application/json');

if (!isset($_GET['grade'])) {
    echo json_encode([]);
    exit;
}

$grade = $_GET['grade'];

// Get unique subjects for the selected grade from student_subjects table
// Assuming student_subjects table has student_id and subject columns, and students table has id and grade columns
$subjects_query = "SELECT DISTINCT ss.subject FROM student_subjects ss INNER JOIN students s ON ss.student_id = s.id WHERE s.grade = ? ORDER BY ss.subject ASC";
$stmt = $conn->prepare($subjects_query);
$stmt->bind_param("s", $grade);
$stmt->execute();
$result = $stmt->get_result();

$subjects = [];
while ($row = $result->fetch_assoc()) {
    $subjects[] = $row['subject'];
}

echo json_encode($subjects);

$stmt->close();
$conn->close();
?> 