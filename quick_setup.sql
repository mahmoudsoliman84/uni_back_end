-- Quick Setup for Users Table
-- Run this in your PostgreSQL database

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'TEACHER', 'STUDENT')),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE
);

-- Insert admin user (password: admin123)
INSERT INTO users (username, password, email, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired)
VALUES (
    'admin',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi',
    'admin@university.com',
    'Admin',
    'User',
    'ADMIN',
    TRUE,
    TRUE,
    TRUE,
    TRUE
) ON CONFLICT (username) DO NOTHING;

-- Verify the setup
SELECT 'Users table created successfully!' as status;
SELECT username, role, enabled FROM users;
