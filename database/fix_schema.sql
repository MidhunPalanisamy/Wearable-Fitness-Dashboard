-- Drop and recreate users table with correct schema
DROP TABLE IF EXISTS alerts;
DROP TABLE IF EXISTS heart_rate_log;
DROP TABLE IF EXISTS daily_metrics;
DROP TABLE IF EXISTS otp;
DROP TABLE IF EXISTS users;

-- Users Table (Updated)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    mobile_number VARCHAR(15) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL
);

-- OTP Table
CREATE TABLE otp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mobile_number VARCHAR(15) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    expiry_time DATETIME NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_mobile (mobile_number)
);

-- Daily Metrics Table
CREATE TABLE daily_metrics (
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
CREATE TABLE heart_rate_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    timestamp DATETIME NOT NULL,
    heart_rate INT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_timestamp (user_id, timestamp)
);

-- Alerts Table
CREATE TABLE alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    severity VARCHAR(50) NOT NULL,
    timestamp DATETIME NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_timestamp (user_id, timestamp)
);
