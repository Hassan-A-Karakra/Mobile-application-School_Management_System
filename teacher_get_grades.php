<?php
require_once 'db.php';
header('Content-Type: application/json');

// Get unique grades from students table
$grades_query = "SELECT DISTINCT grade FROM students ORDER BY grade ASC";
$grades_result = $conn->query($grades_query);

$grades = [];
while ($row = $grades_result->fetch_assoc()) {
    // Exclude empty or null grades
    if (!empty($row['grade'])) {
        $grades[] = $row['grade'];
    }
}

echo json_encode($grades);

$conn->close();
?> 