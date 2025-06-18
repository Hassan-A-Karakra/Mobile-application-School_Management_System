<?php
require_once 'db.php';
header('Content-Type: application/json');

$data = json_decode(file_get_contents('php://input'), true);
$student_id = isset($data['student_id']) ? (int)$data['student_id'] : 0;

error_log("Received student_id: " . $student_id);

if ($student_id <= 0) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid student ID'
    ]);
    exit;
}

try {
    $stmt_subjects = $conn->prepare("SELECT subject FROM student_subjects WHERE student_id = ?");
    if (!$stmt_subjects) {
        throw new Exception("Prepare failed for student_subjects: " . $conn->error);
    }
    $stmt_subjects->bind_param("i", $student_id);
    if (!$stmt_subjects->execute()) {
        throw new Exception("Execute failed for student_subjects: " . $stmt_subjects->error);
    }
    $result_subjects = $stmt_subjects->get_result();
    $student_subjects = [];
    while ($row = $result_subjects->fetch_assoc()) {
        $student_subjects[] = $row['subject'];
    }
    $stmt_subjects->close();

    if (empty($student_subjects)) {
        echo json_encode([
            'status' => 'success',
            'teachers' => [],
            'message' => 'Student is not enrolled in any subjects.'
        ]);
        exit;
    }

    $placeholders = implode(',', array_fill(0, count($student_subjects), '?'));
    $types = str_repeat('s', count($student_subjects));

    $sql_teachers = "SELECT DISTINCT id, name, email, subject FROM teachers WHERE subject IN ($placeholders) ORDER BY name";
    $stmt_teachers = $conn->prepare($sql_teachers);
    if (!$stmt_teachers) {
        throw new Exception("Prepare failed for teachers: " . $conn->error);
    }
    
    $params = array_merge([$types], $student_subjects);
    call_user_func_array([$stmt_teachers, 'bind_param'], refValues($params));

    if (!$stmt_teachers->execute()) {
        throw new Exception("Execute failed for teachers: " . $stmt_teachers->error);
    }
    
    $result_teachers = $stmt_teachers->get_result();
    $teachers = [];
    while ($row = $result_teachers->fetch_assoc()) {
        $teachers[] = [
            'id' => $row['id'],
            'name' => $row['name'],
            'email' => $row['email'],
            'subject' => $row['subject']
        ];
    }
    $stmt_teachers->close();

    echo json_encode([
        'status' => 'success',
        'teachers' => $teachers
    ]);

} catch (Exception $e) {
    error_log("Error in student_get_teachers_for_student.php: " . $e->getMessage());
    echo json_encode([
        'status' => 'error',
        'message' => 'Database error: ' . $e->getMessage()
    ]);
} finally {
    $conn->close();
}

function refValues($arr){
    if (strnatcmp(phpversion(),'5.3') >= 0) {
        $refs = array();
        foreach($arr as $key => $value)
            $refs[$key] = &$arr[$key];
        return $refs;
    }
    return $arr;
}
?>
