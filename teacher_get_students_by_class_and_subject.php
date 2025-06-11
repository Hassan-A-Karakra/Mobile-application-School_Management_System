<?php
require_once 'db.php';
header('Content-Type: application/json');

// Check if required parameters are set
if (!isset($_GET['class_name']) || !isset($_GET['subject'])) {
    echo json_encode(["success" => false, "students" => [], "message" => "Class name or subject not provided."]);
    exit;
}

$class_name = $_GET['class_name'];
$subject = $_GET['subject'];

// Fetch students in the specified class and subject, including their total absence count
$query = "SELECT s.id, s.name, s.email, s.age, COALESCE(SUM(a.absence_count), 0) AS total_absence_count
          FROM students s
          INNER JOIN student_subjects ss ON s.id = ss.student_id
          LEFT JOIN attendance a ON s.id = a.student_id AND a.subject = ?
          WHERE s.grade = ? AND ss.subject = ?
          GROUP BY s.id, s.name, s.email, s.age
          ORDER BY s.name ASC";

$stmt = $conn->prepare($query);
$stmt->bind_param("sis", $subject, $class_name, $subject); // Assuming grade is INT, subject is STRING
$stmt->execute();
$result = $stmt->get_result();

$students = [];
while ($row = $result->fetch_assoc()) {
    $students[] = [
        'id' => $row['id'],
        'name' => $row['name'],
        'email' => $row['email'],
        'age' => $row['age'],
        'absence_count' => (int)$row['total_absence_count']
    ];
}

echo json_encode(["success" => true, "students" => $students, "message" => "Students fetched successfully."]);

$stmt->close();
$conn->close();
?> 