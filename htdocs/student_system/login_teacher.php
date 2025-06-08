// Get JSON data from the request body
$data = json_decode(file_get_contents('php://input'), true);

// Add this line to log the raw decoded data
error_log("Decoded data: " . var_export($data, true));

// Validate input
if (!isset($data['email']) || !isset($data['password'])) {
    echo json_encode([
        'status' => 'error',
        'message' => 'Missing email or password'
    ]);
    exit;
}

$email = $data['email'];
$password = $data['password'];

// Add these lines to log the exact email and password before using them in the query
error_log("Email before query: '" . $email . "' (length: " . strlen($email) . ")");
error_log("Password before query: '" . $password . "' (length: " . strlen($password) . ")");


// Use a prepared statement to prevent SQL injection
$stmt = $conn->prepare("SELECT id, name, email FROM teachers WHERE email = ? AND password = ?");

// Add error checking for prepare statement
if ($stmt === false) {
    error_log("Prepare failed: " . $conn->error);
    echo json_encode([
        'status' => 'error',
        'message' => 'Database error: Prepare failed'
    ]);
    exit;
}

$stmt->bind_param("ss", $email, $password);

// Add error checking for execute statement
if (!$stmt->execute()) {
    error_log("Execute failed: " . $stmt->error);
    echo json_encode([
        'status' => 'error',
        'message' => 'Database error: Execute failed'
    ]);
    exit;
}

$result = $stmt->get_result();

// Log the number of rows found
error_log("Number of rows found: " . $result->num_rows);

if ($result->num_rows > 0) {
    $teacher = $result->fetch_assoc();
    // Log the fetched teacher data
    error_log("Teacher data fetched: " . var_export($teacher, true));
    echo json_encode([
        'status' => 'success',
        'message' => 'Login successful',
        'teacher' => $teacher
    ]);
} else {
    echo json_encode([
        'status' => 'error',
        'message' => 'Invalid email or password'
    ]);
}

$stmt->close();
$conn->close();
?> 