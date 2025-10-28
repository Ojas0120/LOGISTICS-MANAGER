-- Add dummy users to existing database
-- Run this script to insert/update login credentials for testing

-- For MySQL
USE logistics_db;

-- Remove existing test users (optional - comment out if you want to keep existing data)
-- DELETE FROM users WHERE username IN ('admin', 'user', 'admin2', 'john', 'sarah', 'mike');

-- Insert dummy users
INSERT INTO users (username, password, email, role) VALUES 
('admin', 'admin123', 'admin@logistics.com', 'ADMIN'),
('user', 'user123', 'user@logistics.com', 'USER'),
('admin2', 'admin456', 'admin2@logistics.com', 'ADMIN'),
('john', 'john123', 'john@example.com', 'USER'),
('sarah', 'sarah123', 'sarah@example.com', 'USER'),
('mike', 'mike123', 'mike@example.com', 'USER')
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    email = VALUES(email),
    role = VALUES(role);

-- Verify users were added
SELECT username, password, role FROM users;


