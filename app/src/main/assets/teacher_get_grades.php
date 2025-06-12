<?php
require_once 'db.php';
header('Content-Type: application/json; charset=utf-8');

$grades = [];
$sql = "SELECT DISTINCT grade FROM students ORDER BY grade ASC";
$result = $conn->query($sql);

if ($result) {
    while ($row = $result->fetch_assoc()) {
        $grades[] = $row['grade'];
    }
    echo json_encode([
        "status" => "success",
        "grades" => $grades
    ], JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "Error fetching grades: " . $conn->error
    ]);
}

$conn->close();
?> 