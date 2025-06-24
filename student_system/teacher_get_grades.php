    <?php
    require_once 'db.php';
    header('Content-Type: application/json; charset=utf-8');

    $grades = [];
    $sql = "SELECT DISTINCT grade FROM students ORDER BY grade ASC";
    $result = $conn->query($sql);

    if ($result) {
        while ($row = $result->fetch_assoc()) {
            $grades[] = $row['grade'];
        }
    } else {
        error_log("Error fetching grades: " . $conn->error);
    }

    echo json_encode($grades, JSON_UNESCAPED_UNICODE);
    $conn->close();
    ?>