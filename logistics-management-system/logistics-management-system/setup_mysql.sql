-- MySQL Setup Script for Logistics Management System
-- Run this script in MySQL to create the database and user

-- Create database
CREATE DATABASE IF NOT EXISTS logistics_db;
USE logistics_db;

-- Create user (optional - you can use root user)
-- CREATE USER 'logistics_user'@'localhost' IDENTIFIED BY 'logistics_password';
-- GRANT ALL PRIVILEGES ON logistics_db.* TO 'logistics_user'@'localhost';
-- FLUSH PRIVILEGES;

-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default users with dummy credentials
INSERT IGNORE INTO users (username, password, email, role) VALUES 
('admin', 'admin123', 'admin@logistics.com', 'ADMIN'),
('user', 'user123', 'user@logistics.com', 'USER'),
('admin2', 'admin456', 'admin2@logistics.com', 'ADMIN'),
('john', 'john123', 'john@example.com', 'USER'),
('sarah', 'sarah123', 'sarah@example.com', 'USER'),
('mike', 'mike123', 'mike@example.com', 'USER');

-- Note: Other tables (courier_companies, shipments, etc.) will be automatically created
-- when the application starts. The tables are defined in database_schema.sql

-- Make sure MySQL is running on localhost:3306
-- Default credentials: root/password (change in DatabaseConnection.java if needed)
