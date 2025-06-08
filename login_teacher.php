<?php

$stmt = $conn->prepare("SELECT id, name, email FROM teachers WHERE email = ? AND password = ?");

$stmt->execute([$email, $password]);

$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $teacher = $result->fetch_assoc();
    // ... existing code ...
} else {
    // ... existing code ...
} 