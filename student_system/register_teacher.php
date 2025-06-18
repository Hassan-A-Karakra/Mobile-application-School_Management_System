<?php

error_reporting(E_ALL);
ini_set('display_errors', 1);

header('Content-Type: application/json');

ob_start();

require_once 'db.php';

if (!isset($_POST['name'], $_POST['email'], $_POST['password'], $_POST['subject'])) {
    $output = ob_get_clean();
    if (!empty($output)) {
        echo json_encode(["status" => "error", "message" => "Missing required fields. Unexpected output: " . $output]);
    } else {
        echo json_encode(["status" => "error", "message" => "Missing required fields."]);
    }
    exit;
}

$name = $_POST['name'];
$email = $_POST['email'];
$password = $_POST['password'];
$subject = $_POST['subject'];

$day = $_POST['day'] ?? NULL;
$time = $_POST['time'] ?? NULL;
$grade = $_POST['grade'] ?? NULL;

$check = $conn->prepare("SELECT id FROM teachers WHERE email = ?");
$check->bind_param("s", $email);
$check->execute();
$check->store_result();

if ($check->num_rows > 0) {
    $output = ob_get_clean();
    if (!empty($output)) {
        echo json_encode(["status" => "error", "message" => "Email already exists. Unexpected output: " . $output]);
    } else {
        echo json_encode(["status" => "error", "message" => "Email already exists."]);
    }
    $check->close();
    $conn->close();
    exit;
}
$check->close();

$stmt = $conn->prepare("INSERT INTO teachers (name, email, password, subject, day, time, grade) VALUES (?, ?, ?, ?, ?, ?, ?)");
$stmt->bind_param("sssssss", $name, $email, $password, $subject, $day, $time, $grade);

if ($stmt->execute()) {
    $output = ob_get_clean();
    if (!empty($output)) {
        echo json_encode(["status" => "error", "message" => "Teacher registered successfully, but with unexpected output: " . $output]);
    } else {
        echo json_encode(["status" => "success", "message" => "Teacher registered successfully"]);
    }
} else {
    $output = ob_get_clean();
    if (!empty($output)) {
        echo json_encode(["status" => "error", "message" => "Error inserting teacher: " . $stmt->error . " Unexpected output: " . $output]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error inserting teacher: " . $stmt->error]);
    }
}

$stmt->close();
$conn->close();
?>
