<?php
require_once 'db.php';
header('Content-Type: application/json');

// Get unique grades from students table
$grades_query = "SELECT DISTINCT grade FROM students ORDER BY grade ASC";
$grades_result = $conn->query($grades_query);

$grades = [];
while ($row = $grades_result->fetch_assoc()) {
    $grades[] = $row['grade'];
}

// Get unique subjects from student_subjects table
$subjects_query = "SELECT DISTINCT subject FROM student_subjects ORDER BY subject ASC";
$subjects_result = $conn->query($subjects_query);

$subjects = [];
while ($row = $subjects_result->fetch_assoc()) {
    $subjects[] = $row['subject'];
}

echo json_encode([
    'status' => 'success',
    'grades' => $grades,
    'subjects' => $subjects
]);

$conn->close();
?> 