-- Test Users for Authentication
-- Password for all users is 'password123' (BCrypt encoded)

-- Admin User
INSERT INTO users (username, password, email, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES (
    'admin',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', -- password123
    'admin@university.com',
    'Admin',
    'User',
    'ADMIN',
    true,
    true,
    true,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;

-- Teacher User
INSERT INTO users (username, password, email, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES (
    'teacher',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', -- password123
    'teacher@university.com',
    'John',
    'Teacher',
    'TEACHER',
    true,
    true,
    true,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;

-- Student User
INSERT INTO users (username, password, email, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES (
    'student',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', -- password123
    'student@university.com',
    'Jane',
    'Student',
    'STUDENT',
    true,
    true,
    true,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;

-- Test User
INSERT INTO users (username, password, email, first_name, last_name, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at)
VALUES (
    'testuser',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', -- password123
    'test@university.com',
    'Test',
    'User',
    'STUDENT',
    true,
    true,
    true,
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
) ON CONFLICT (username) DO NOTHING;
