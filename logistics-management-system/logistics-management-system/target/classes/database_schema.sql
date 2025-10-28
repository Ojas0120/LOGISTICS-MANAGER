-- Logistics Management System Database Schema
-- MySQL Database Setup

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS logistics_db;
USE logistics_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Courier companies table
CREATE TABLE IF NOT EXISTS courier_companies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price_per_km DECIMAL(10,2) NOT NULL,
    price_per_kg DECIMAL(10,2) NOT NULL,
    base_handling_time INT NOT NULL, -- in hours
    transport_modes VARCHAR(50) NOT NULL, -- comma-separated: AIR,ROAD,RAIL
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Shipments table
CREATE TABLE IF NOT EXISTS shipments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    sender_city VARCHAR(100) NOT NULL,
    receiver_city VARCHAR(100) NOT NULL,
    distance DECIMAL(10,2) NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    priority ENUM('LOW', 'NORMAL', 'HIGH', 'URGENT') NOT NULL,
    traffic_condition ENUM('LOW', 'NORMAL', 'HIGH') NOT NULL,
    transport_mode ENUM('AIR', 'ROAD', 'RAIL') NOT NULL,
    selected_company_id INT, -- Selected company for this shipment
    status ENUM('PENDING', 'CONFIRMED', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (selected_company_id) REFERENCES courier_companies(id)
);

-- Shipment calculations table (stores calculated costs for each company)
CREATE TABLE IF NOT EXISTS shipment_calculations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    shipment_id INT NOT NULL,
    company_id INT NOT NULL,
    total_cost DECIMAL(10,2) NOT NULL,
    estimated_delivery_time INT NOT NULL, -- in hours
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shipment_id) REFERENCES shipments(id),
    FOREIGN KEY (company_id) REFERENCES courier_companies(id)
);

-- Insert default users with dummy credentials
INSERT IGNORE INTO users (username, password, email, role) VALUES 
('admin', 'admin123', 'admin@logistics.com', 'ADMIN'),
('user', 'user123', 'user@logistics.com', 'USER'),
('admin2', 'admin456', 'admin2@logistics.com', 'ADMIN'),
('john', 'john123', 'john@example.com', 'USER'),
('sarah', 'sarah123', 'sarah@example.com', 'USER'),
('mike', 'mike123', 'mike@example.com', 'USER');

-- Insert 25 diverse courier companies with different rates (in Rupees)
INSERT IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES 
-- Premium Air Services
('SkyExpress Premium', 4.50, 28.00, 1, 'AIR'),
('AeroLogistics Elite', 5.20, 32.00, 1, 'AIR'),
('JetCargo International', 4.80, 30.00, 1, 'AIR'),
('AirSpeed Express', 3.90, 25.00, 2, 'AIR'),
('Falcon Delivery', 4.20, 26.00, 1, 'AIR'),

-- Standard Air Services
('Cloud Courier', 3.20, 20.00, 2, 'AIR'),
('Wing Logistics', 3.80, 18.00, 3, 'AIR'),
('SkyLink Transport', 3.50, 22.00, 2, 'AIR'),
('AirBridge Express', 3.00, 19.00, 2, 'AIR'),
('Eagle Cargo', 3.70, 24.00, 1, 'AIR'),

-- Premium Road Services
('RoadMaster Premium', 2.80, 16.00, 2, 'ROAD'),
('Highway Express Elite', 3.20, 20.00, 2, 'ROAD'),
('TruckStar Logistics', 2.50, 15.00, 3, 'ROAD'),
('RoadRunner Express', 2.90, 17.00, 2, 'ROAD'),
('Velocity Transport', 3.10, 19.00, 2, 'ROAD'),

-- Standard Road Services
('City Logistics', 1.50, 10.00, 3, 'ROAD'),
('Metro Delivery', 1.80, 12.00, 4, 'ROAD'),
('Urban Express', 2.00, 13.00, 3, 'ROAD'),
('Local Logistics', 1.20, 8.00, 5, 'ROAD'),
('Quick Delivery', 2.20, 14.00, 3, 'ROAD'),

-- Rail Services
('RailCargo Express', 1.80, 12.00, 4, 'RAIL'),
('Iron Horse Logistics', 1.60, 11.00, 5, 'RAIL'),
('TrackMaster', 2.00, 13.00, 4, 'RAIL'),
('RailBridge Transport', 1.70, 12.00, 4, 'RAIL'),
('Steel Wheels', 1.90, 13.00, 4, 'RAIL'),

-- Multi-Modal Services
('Universal Logistics', 2.50, 15.00, 3, 'AIR,ROAD,RAIL'),
('Global Express', 3.00, 18.00, 2, 'AIR,ROAD'),
('MultiMode Transport', 2.20, 14.00, 3, 'ROAD,RAIL'),
('FlexiCargo', 2.80, 17.00, 2, 'AIR,ROAD,RAIL'),
('Omni Logistics', 2.60, 16.00, 3, 'AIR,ROAD,RAIL');
