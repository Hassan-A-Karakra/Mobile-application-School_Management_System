<?php
require_once 'db.php';
header('Content-Type: application/json');

$query = "SELECT id, name, email, age FROM students ORDER BY name ASC";
$result = mysqli_query($conn, $query);

$students = [];

if ($result && mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $students[] = $row;
    }

    echo json_encode([
        'status' => 'success',
        'students' => $students
    ]);
} else {
    echo json_encode([
        'status' => 'empty',
        'students' => []
    ]);
}
?>
