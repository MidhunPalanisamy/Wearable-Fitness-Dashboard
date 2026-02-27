-- Create Database
CREATE DATABASE IF NOT EXISTS fitness_dashboard;
USE fitness_dashboard;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    mobile_number VARCHAR(15) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL
);

-- OTP Table
CREATE TABLE IF NOT EXISTS otp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mobile_number VARCHAR(15) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    expiry_time DATETIME NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_mobile (mobile_number)
);

-- Daily Metrics Table
CREATE TABLE IF NOT EXISTS daily_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    steps INT NOT NULL,
    calories INT NOT NULL,
    sleep_hours DOUBLE NOT NULL,
    deep_sleep DOUBLE NOT NULL,
    light_sleep DOUBLE NOT NULL,
    rem_sleep DOUBLE NOT NULL,
    avg_heart_rate INT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_date (user_id, date)
);

-- Heart Rate Log Table
CREATE TABLE IF NOT EXISTS heart_rate_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp DATETIME NOT NULL,
    heart_rate INT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_timestamp (user_id, timestamp)
);

-- Alerts Table
CREATE TABLE IF NOT EXISTS alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    severity VARCHAR(50) NOT NULL,
    timestamp DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_timestamp (user_id, timestamp)
);
