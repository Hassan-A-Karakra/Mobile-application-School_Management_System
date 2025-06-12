<?php
require_once 'db_connect.php';

// Query to get unique classes
$sql = "SELECT DISTINCT class FROM assignments ORDER BY class ASC";

$result = $conn->query($sql);

if ($result) {
    $classes = [];
    while ($row = $result->fetch_assoc()) {
        $classes[] = $row['class'];
    }
    
    echo json_encode([
        'status' => 'success',
        'classes' => $classes
    ]);
} else {
    echo json_encode([
        'status' => 'error',
        'message' => 'Error fetching classes: ' . $conn->error
    ]);
}

$conn->close();
?> 