-- =====================================================
-- University Management System - Database Setup Script
-- =====================================================

-- Create the users table for authentication
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
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for better performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- Insert the initial admin user
-- Note: This password is 'admin123' encrypted with BCrypt
-- You can generate a new encrypted password using the backend application
INSERT INTO users (username, password, email, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired)
VALUES (
    'admin',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- admin123
    'admin@university.com',
    'Admin',
    'User',
    'ADMIN',
    TRUE,
    TRUE,
    TRUE,
    TRUE
) ON CONFLICT (username) DO NOTHING;

-- Create a function to update the updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to automatically update updated_at
CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Grant necessary permissions (adjust as needed for your setup)
-- GRANT ALL PRIVILEGES ON TABLE users TO your_database_user;
-- GRANT USAGE, SELECT ON SEQUENCE users_id_seq TO your_database_user;

-- =====================================================
-- Sample Data for Testing (Optional)
-- =====================================================

-- Insert sample teacher
INSERT INTO users (username, password, email, first_name, last_name, role, enabled)
VALUES (
    'teacher1',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- admin123 (change this in production)
    'teacher1@university.com',
    'John',
    'Smith',
    'TEACHER',
    TRUE
) ON CONFLICT (username) DO NOTHING;

-- Insert sample student
INSERT INTO users (username, password, email, first_name, last_name, role, enabled)
VALUES (
    'student1',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', -- admin123 (change this in production)
    'student1@university.com',
    'Alice',
    'Johnson',
    'STUDENT',
    TRUE
) ON CONFLICT (username) DO NOTHING;

-- =====================================================
-- Verification Queries
-- =====================================================

-- Check if users table was created
SELECT table_name, column_name, data_type, is_nullable
FROM information_schema.columns 
WHERE table_name = 'users' 
ORDER BY ordinal_position;

-- Check if admin user was inserted
SELECT id, username, email, first_name, last_name, role, enabled
FROM users 
WHERE role = 'ADMIN';

-- Count users by role
SELECT role, COUNT(*) as user_count
FROM users 
GROUP BY role;

-- =====================================================
-- Cleanup Commands (if needed)
-- =====================================================

-- To remove the users table (be careful!)
-- DROP TABLE IF EXISTS users CASCADE;

-- To remove specific users
-- DELETE FROM users WHERE username = 'teacher1';
-- DELETE FROM users WHERE username = 'student1';

-- To reset the sequence
-- ALTER SEQUENCE users_id_seq RESTART WITH 1;
