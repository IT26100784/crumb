-- ========================================================
-- Crumb Bakery Platform - User Management Database Script
-- Module: SE1020 - Object Oriented Programming
-- Feature: User Management (CRUD & Authentication)
-- ========================================================

CREATE DATABASE IF NOT EXISTS crumb;
USE crumb;

-- Drop table if exists for fresh run
DROP TABLE IF EXISTS users;

-- Users table schema
CREATE TABLE users (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',  -- 'CUSTOMER' or 'ADMIN'
    phone VARCHAR(20),
    address VARCHAR(255),
    customer_type VARCHAR(20) DEFAULT 'Regular',   -- 'Regular' or 'Premium'
    department VARCHAR(50) DEFAULT 'Management',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Seed initial test accounts
INSERT INTO users (id, name, email, password, role, phone, address, customer_type, department)
VALUES
('A001', 'Crumb Owner', 'admin@crumb.lk', 'admin123', 'ADMIN', '', '', '', 'Management'),
('C001', 'Alex Perera', 'alex@example.com', 'alex123', 'CUSTOMER', '077 123 4567', '24 Flower Road, Colombo', 'Regular', '');

-- Sample verification query
SELECT id, name, email, role, customer_type, department FROM users;
