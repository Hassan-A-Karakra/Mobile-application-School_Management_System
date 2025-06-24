<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "school_management";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Database connection failed: " . $conn->connect_error]);
    exit();
}

if (isset($_GET['teacher_id'])) {
    $teacherId = $conn->real_escape_string($_GET['teacher_id']);

    $sql = "SELECT subject, day, time, grade FROM teachers WHERE id = ?";

    $stmt = $conn->prepare($sql);
    
    if ($stmt === false) {
        echo json_encode(["status" => "error", "message" => "Failed to prepare statement: " . $conn->error]);
        exit();
    }

    $stmt->bind_param("i", $teacherId);
    $stmt->execute();
    $result = $stmt->get_result();

    $schedule = [];
    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            $schedule[] = $row;
        }
        echo json_encode(["status" => "success", "message" => "Schedule retrieved successfully.", "schedule" => $schedule]);
    } else {
        echo json_encode(["status" => "error", "message" => "No schedule found for this teacher.", "schedule" => []]);
    }

    $stmt->close();
} else {
    echo json_encode(["status" => "error", "message" => "Teacher ID (teacher_id) not provided."]);
}

$conn->close();
?>
