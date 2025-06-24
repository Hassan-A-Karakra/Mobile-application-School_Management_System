<?php
require 'db.php';

$result = $conn->query("SELECT id, name, email, subject FROM teachers");
$teachers = [];

while ($row = $result->fetch_assoc()) {
    $teachers[] = $row;
}

echo json_encode(["status" => "success", "teachers" => $teachers]);
?>