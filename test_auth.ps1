# Test Authentication Script for University Management System
# This script tests the authentication endpoints

Write-Host "=== University Management System - Authentication Test ===" -ForegroundColor Green
Write-Host ""

# Base URL
$baseUrl = "http://localhost:8080/api"

# Test 1: Test public endpoint
Write-Host "1. Testing public endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/auth/public/test" -Method GET
    Write-Host "   ✓ Public endpoint working: $($response.Content)" -ForegroundColor Green
} catch {
    Write-Host "   ✗ Public endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 2: Test login with admin user
Write-Host "2. Testing admin login..." -ForegroundColor Yellow
try {
    $loginData = @{
        username = "admin"
        password = "password123"
    } | ConvertTo-Json

    $response = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method POST -Body $loginData -ContentType "application/json"
    $loginResult = $response.Content | ConvertFrom-Json
    
    if ($loginResult.token) {
        Write-Host "   ✓ Admin login successful" -ForegroundColor Green
        Write-Host "   Token: $($loginResult.token.Substring(0, 50))..." -ForegroundColor Cyan
        Write-Host "   Role: $($loginResult.role)" -ForegroundColor Cyan
        $adminToken = $loginResult.token
    } else {
        Write-Host "   ✗ Admin login failed: $($loginResult.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "   ✗ Admin login failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 3: Test login with teacher user
Write-Host "3. Testing teacher login..." -ForegroundColor Yellow
try {
    $loginData = @{
        username = "teacher"
        password = "password123"
    } | ConvertTo-Json

    $response = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method POST -Body $loginData -ContentType "application/json"
    $loginResult = $response.Content | ConvertFrom-Json
    
    if ($loginResult.token) {
        Write-Host "   ✓ Teacher login successful" -ForegroundColor Green
        Write-Host "   Token: $($loginResult.token.Substring(0, 50))..." -ForegroundColor Cyan
        Write-Host "   Role: $($loginResult.role)" -ForegroundColor Cyan
        $teacherToken = $loginResult.token
    } else {
        Write-Host "   ✗ Teacher login failed: $($loginResult.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "   ✗ Teacher login failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 4: Test login with student user
Write-Host "4. Testing student login..." -ForegroundColor Yellow
try {
    $loginData = @{
        username = "student"
        password = "password123"
    } | ConvertTo-Json

    $response = Invoke-WebRequest -Uri "$baseUrl/auth/login" -Method POST -Body $loginData -ContentType "application/json"
    $loginResult = $response.Content | ConvertFrom-Json
    
    if ($loginResult.token) {
        Write-Host "   ✓ Student login successful" -ForegroundColor Green
        Write-Host "   Token: $($loginResult.token.Substring(0, 50))..." -ForegroundColor Cyan
        Write-Host "   Role: $($loginResult.role)" -ForegroundColor Cyan
        $studentToken = $loginResult.token
    } else {
        Write-Host "   ✗ Student login failed: $($loginResult.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "   ✗ Student login failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""

# Test 5: Test authenticated endpoint with admin token
if ($adminToken) {
    Write-Host "5. Testing authenticated endpoint with admin token..." -ForegroundColor Yellow
    try {
        $headers = @{
            "Authorization" = "Bearer $adminToken"
            "Content-Type" = "application/json"
        }
        
        $response = Invoke-WebRequest -Uri "$baseUrl/auth/me" -Method GET -Headers $headers
        $userInfo = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✓ Authenticated endpoint working" -ForegroundColor Green
        Write-Host "   Username: $($userInfo.username)" -ForegroundColor Cyan
        Write-Host "   Authenticated: $($userInfo.authenticated)" -ForegroundColor Cyan
    } catch {
        Write-Host "   ✗ Authenticated endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""

# Test 6: Test user registration
Write-Host "6. Testing user registration..." -ForegroundColor Yellow
try {
    $registrationData = @{
        username = "newuser"
        password = "password123"
        email = "newuser@university.com"
        firstName = "New"
        lastName = "User"
        role = "STUDENT"
    } | ConvertTo-Json

    $response = Invoke-WebRequest -Uri "$baseUrl/auth/register" -Method POST -Body $registrationData -ContentType "application/json"
    $registrationResult = $response.Content | ConvertFrom-Json
    
    if ($registrationResult.token) {
        Write-Host "   ✓ User registration successful" -ForegroundColor Green
        Write-Host "   Username: $($registrationResult.username)" -ForegroundColor Cyan
        Write-Host "   Role: $($registrationResult.role)" -ForegroundColor Cyan
        Write-Host "   Token: $($registrationResult.token.Substring(0, 50))..." -ForegroundColor Cyan
    } else {
        Write-Host "   ✗ User registration failed: $($registrationResult.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "   ✗ User registration failed: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "=== Authentication Test Complete ===" -ForegroundColor Green
