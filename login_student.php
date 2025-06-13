<?php
// Disable error reporting to prevent PHP warnings/errors from corrupting JSON output
error_reporting(0);
ini_set('display_errors', 0);

$host = 'localhost';
$username = 'root'; 
$password = '';
$database = 'school_management'; // Ensure this matches your database name

$conn = new mysqli($host, $username, $password, $database);

if ($conn->connect_error) {
    // Return a JSON error response even for connection failures
    http_response_code(500); // Internal Server Error
    echo json_encode(['success' => false, 'message' => 'Database connection failed: ' . $conn->connect_error]);
    exit;
}

// Set headers for JSON response
header('Content-Type: application/json');

// Get POST data
$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';

// Basic validation for missing data
if (empty($email) || empty($password)) {
    http_response_code(400); // Bad Request
    echo json_encode([
        'success' => false,
        'message' => 'Please enter both email and password'
    ]);
    $conn->close(); // Close connection before exiting
    exit;
}

// Simple query to check credentials against plain text password
// Selecting 'age' to match your 'students' table structure
$sql = "SELECT id, name, email, grade, age FROM students WHERE email = '$email' AND password = '$password'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $student = $result->fetch_assoc();
    
    // Start session to maintain login state
    session_start();
    $_SESSION['student_id'] = $student['id']; // Store student ID in session
    
    echo json_encode([
        'success' => true,
        'message' => 'Login successful',
        'student' => $student
    ]);
} else {
    http_response_code(401); // Unauthorized
    echo json_encode([
        'success' => false,
        'message' => 'Invalid email or password'
    ]);
}

$conn->close();
?> 
