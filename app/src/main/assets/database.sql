-- Create database
CREATE DATABASE IF NOT EXISTS school_management;
USE school_management;

-- Create teachers table
CREATE TABLE IF NOT EXISTS teachers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    subject VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample data
INSERT INTO teachers (name, email, subject) VALUES
('أحمد محمد', 'ahmed@school.com', 'الرياضيات'),
('سارة أحمد', 'sara@school.com', 'اللغة العربية'),
('محمد علي', 'mohammed@school.com', 'العلوم'); 