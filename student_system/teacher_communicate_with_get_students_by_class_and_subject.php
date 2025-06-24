<?php
require_once 'db.php';

header('Content-Type: application/json; charset=utf-8');

$response = array();
$response['success'] = false;
$response['students'] = array();

if (isset($_GET['grade']) && isset($_GET['subject'])) {
    $grade = $conn->real_escape_string($_GET['grade']);
    $subject = $conn->real_escape_string($_GET['subject']);

    $sql = "SELECT 
                s.id, 
                s.name, 
                s.email, 
                s.age,
                COALESCE(g.grade, '') AS score,
                COALESCE(a.absence_count, 0) AS total_absence_count
            FROM 
                students s
            JOIN 
                student_subjects ss ON s.id = ss.student_id
            LEFT JOIN 
                grades g ON s.id = g.student_id AND g.subject_name = ?
            LEFT JOIN 
                attendance a ON s.id = a.student_id AND a.subject = ?
            WHERE 
                s.grade = ? AND ss.subject = ?
            ORDER BY 
                s.name ASC";

    $stmt = $conn->prepare($sql);
    if ($stmt === false) {
        $response['message'] = "Failed to prepare statement: " . $conn->error;
        echo json_encode($response, JSON_UNESCAPED_UNICODE);
        exit;
    }
    $stmt->bind_param("ssss", $subject, $subject, $grade, $subject);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $response['success'] = true;
        while($row = $result->fetch_assoc()) {
            $response['students'][] = array(
                'id' => $row['id'],
                'name' => $row['name'],
                'email' => $row['email'],
                'age' => $row['age'],
                'score' => $row['score'] ?? '',
                'absence_count' => (int)$row['total_absence_count']
            );
        }
    } else {
        $response['message'] = "No students found for this class and subject.";
    }
    $stmt->close();
} else {
    $response['message'] = "Class or subject not provided.";
    $response['debug_get'] = $_GET;
}

echo json_encode($response, JSON_UNESCAPED_UNICODE);

$conn->close();
?>
