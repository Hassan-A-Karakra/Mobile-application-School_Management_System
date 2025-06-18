<?php
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

$host = 'localhost';
$username = 'root';
$password = '';
$dbname = 'school_management';
$port = 3306;

try {
    $conn = new mysqli($host, $username, $password, $dbname, $port);

    if ($conn->connect_error) {
        throw new Exception('Database connection failed: ' . $conn->connect_error);
    }

    $student_id = $_GET['student_id'] ?? null;
    if (!$student_id) {
        $data = json_decode(file_get_contents("php://input"), true);
        $student_id = $data['student_id'] ?? null;
    }
    if (!$student_id) {
        throw new Exception('Student ID is missing.');
    }

    $stmt = $conn->prepare("SELECT grade FROM students WHERE id = ?");
    if (!$stmt) throw new Exception('Prepare failed: ' . $conn->error);
    $stmt->bind_param("i", $student_id);
    if (!$stmt->execute()) throw new Exception('Execute failed: ' . $stmt->error);
    $result = $stmt->get_result();
    if ($result->num_rows === 0) throw new Exception('Student not found.');
    $grade = $result->fetch_assoc()['grade'];
    $stmt->close();

    $stmt = $conn->prepare(
        "SELECT s.subject, s.day, s.time, t.name as teacher
         FROM schedules s
         LEFT JOIN teachers t ON s.teacher_id = t.id
         WHERE s.grade = ?
         ORDER BY FIELD(s.day, 'Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday'), s.time"
    );
    if (!$stmt) throw new Exception('Prepare failed: ' . $conn->error);
    $stmt->bind_param("s", $grade);
    if (!$stmt->execute()) throw new Exception('Execute failed: ' . $stmt->error);

    $schedule_result = $stmt->get_result();
    $schedule = [];
    while ($row = $schedule_result->fetch_assoc()) {
        $schedule[] = $row;
    }

    echo json_encode([
        'status' => 'success',
        'schedule' => $schedule
    ]);

} catch (Exception $e) {
    echo json_encode([
        'status' => 'error',
        'message' => $e->getMessage()
    ]);
} finally {
    if (isset($conn)) $conn->close();
}
?>
