<?php
require_once 'db_connect.php';

// Get JSON data from request
$data = json_decode(file_get_contents('php://input'), true);

if (!$data || !isset($data['grade'])) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Grade is required'
    ]);
    exit;
}

$grade = $conn->real_escape_string($data['grade']);

// Build the query
$sql = "SELECT * FROM assignments WHERE grade = '$grade' ORDER BY due_date ASC";

$result = $conn->query($sql);

if ($result) {
    $assignments = [];
    while ($row = $result->fetch_assoc()) {
        $assignments[] = [
            'id' => $row['id'],
            'title' => $row['title'],
            'description' => $row['description'],
            'due_date' => $row['due_date'],
            'class' => $row['class'],
            'subject' => $row['subject']
        ];
    }
    
    echo json_encode([
        'status' => 'success',
        'assignments' => $assignments
    ]);
} else {
    echo json_encode([
        'status' => 'error',
        'message' => 'Error fetching assignments: ' . $conn->error
    ]);
}

$conn->close();
?> 