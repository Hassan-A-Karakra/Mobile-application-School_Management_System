<?php
require 'db.php';

$teacher_id = $_POST['teacher_id'];

$result = $conn->query("SELECT * FROM assignments");
$assignments = [];

while ($row = $result->fetch_assoc()) {
    $assignments[] = $row;
}

echo json_encode(["status" => "success", "assignments" => $assignments]);
?>