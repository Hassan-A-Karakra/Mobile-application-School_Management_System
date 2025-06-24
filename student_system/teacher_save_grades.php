<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

require_once 'db.php';

header('Content-Type: application/json; charset=utf-8');

$response = array();
$response['success'] = false;
$response['message'] = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $json_data = file_get_contents("php://input");
    $data = json_decode($json_data, true);

    if (isset($data['grades']) && is_array($data['grades']) && isset($data['subject_name']) && isset($data['publish']) && isset($data['teacher_id'])) {
        $gradesToSave = $data['grades'];
        $subjectName = $conn->real_escape_string($data['subject_name']);
        $publish = (int)$data['publish'];
        $teacherId = (int)$data['teacher_id'];

        $allGradesSaved = true;
        $conn->begin_transaction();

        foreach ($gradesToSave as $gradeData) {
            $studentId = (int)$gradeData['student_id'];
            $grade = $conn->real_escape_string($gradeData['grade'] ?? '0'); 

            $stmt = $conn->prepare("INSERT INTO grades (student_id, subject_name, grade, published, teacher_id, created_at) 
                                       VALUES (?, ?, ?, ?, ?, NOW()) 
                                       ON DUPLICATE KEY UPDATE 
                                       grade = VALUES(grade), 
                                       published = VALUES(published), 
                                       teacher_id = VALUES(teacher_id), 
                                       last_updated = NOW()");

            if ($stmt) {
                $stmt->bind_param("issii", $studentId, $subjectName, $grade, $publish, $teacherId);

                if (!$stmt->execute()) {
                    $allGradesSaved = false;
                    $response['message'] .= "Failed to save grade for student " . $studentId . ": " . $stmt->error . " | ";
                    break;
                }
                $stmt->close();
            } else {
                $allGradesSaved = false;
                $response['message'] .= "Failed to prepare statement: " . $conn->error . " | ";
                break;
            }
        }

        if ($allGradesSaved) {
            $conn->commit();
            $response['success'] = true;
            $response['message'] = "Grades saved successfully.";
        } else {
            $conn->rollback();
            if (empty($response['message'])) {
                $response['message'] = "An error occurred while saving grades.";
            }
        }

    } else {
        $response['message'] = "Invalid data. Received data: " . json_encode($data, JSON_UNESCAPED_UNICODE);
    }
} else {
    $response['message'] = "Invalid request method.";
}

echo json_encode($response, JSON_UNESCAPED_UNICODE);

$conn->close();
?>
