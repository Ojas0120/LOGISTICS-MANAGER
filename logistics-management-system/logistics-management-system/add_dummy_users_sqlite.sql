-- Add dummy users to SQLite database
-- This script inserts/updates login credentials for testing

-- Insert dummy users (INSERT OR IGNORE will skip if they already exist)
INSERT OR IGNORE INTO users (username, password, email, role) VALUES 
('admin', 'admin123', 'admin@logistics.com', 'ADMIN'),
('user', 'user123', 'user@logistics.com', 'USER'),
('admin2', 'admin456', 'admin2@logistics.com', 'ADMIN'),
('john', 'john123', 'john@example.com', 'USER'),
('sarah', 'sarah123', 'sarah@example.com', 'USER'),
('mike', 'mike123', 'mike@example.com', 'USER');

-- Verify users were added
SELECT username, password, role FROM users ORDER BY role, username;


