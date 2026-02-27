# Authentication Upgrade Guide

## Overview
The Wearable Fitness Dashboard has been upgraded with mobile number OTP authentication and user-specific data isolation.

## New Features

### 1. Mobile Number + OTP Authentication
- Users login with mobile number
- 6-digit OTP sent (valid for 5 minutes)
- JWT token issued on successful verification
- No passwords required

### 2. User-Specific Data
- Each user has isolated fitness data
- Data generated on first login
- 30 days of metrics per user
- 24 hours of heart rate logs per user

### 3. JWT Security
- All dashboard endpoints protected
- Token-based authentication
- Automatic token refresh handling
- Secure route protection

## API Endpoints

### Authentication APIs

#### Send OTP
```
POST /api/auth/send-otp
Content-Type: application/json

Request:
{
  "mobileNumber": "9876543210"
}

Response (Success):
{
  "message": "OTP sent successfully",
  "otp": "123456"  // Demo only - shows OTP in response
}

Response (Error):
{
  "error": "Error message"
}
```

#### Verify OTP
```
POST /api/auth/verify-otp
Content-Type: application/json

Request:
{
  "mobileNumber": "9876543210",
  "otp": "123456"
}

Response (Success):
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}

Response (Error - 401):
{
  "error": "Invalid OTP"
}
{
  "error": "OTP expired"
}
{
  "error": "No OTP found"
}
```

### Dashboard APIs (Protected)

All dashboard endpoints require JWT token in Authorization header:

```
Authorization: Bearer <token>
```

#### Get Summary
```
GET /api/dashboard/summary
Authorization: Bearer <token>

Response:
{
  "steps": 8500,
  "calories": 2100,
  "avgHeartRate": 75,
  "sleepHours": 7.5
}
```

#### Get Weekly Metrics
```
GET /api/dashboard/weekly
Authorization: Bearer <token>

Response:
[
  {
    "date": "02/20",
    "steps": 8500,
    "calories": 2100
  },
  ...
]
```

#### Get Heart Rate
```
GET /api/dashboard/heartrate
Authorization: Bearer <token>

Response:
[
  {
    "time": "14:30",
    "heartRate": 75
  },
  ...
]
```

#### Get Alerts
```
GET /api/dashboard/alerts
Authorization: Bearer <token>

Response:
[
  {
    "type": "High Heart Rate",
    "message": "Average heart rate exceeded 95 bpm",
    "severity": "HIGH",
    "timestamp": "2024-02-20 12:00"
  },
  ...
]
```

#### Get Sleep Breakdown
```
GET /api/dashboard/sleep
Authorization: Bearer <token>

Response:
{
  "deepSleep": 2.3,
  "lightSleep": 3.8,
  "remSleep": 1.5
}
```

## Database Changes

### New Tables

#### OTP Table
```sql
CREATE TABLE otp (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mobile_number VARCHAR(15) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    expiry_time DATETIME NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_mobile (mobile_number)
);
```

### Modified Tables

#### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    mobile_number VARCHAR(15) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL
);
```

## Backend Architecture

### New Packages
```
com.fitness.dashboard/
├── auth/
│   ├── AuthController.java
│   ├── AuthService.java
│   └── AuthDTOs.java
├── security/
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
└── util/
    └── JwtUtil.java
```

### Security Flow
1. User sends mobile number → OTP generated
2. User verifies OTP → JWT token issued
3. Token stored in localStorage
4. All API calls include token in Authorization header
5. JwtAuthenticationFilter validates token
6. User ID extracted from token
7. User-specific data returned

## Frontend Changes

### New Pages
- **LoginPage** (`/login`) - Mobile number + OTP entry
- **Dashboard** (`/dashboard`) - Protected dashboard (requires auth)

### New Components
- **ProtectedRoute** - Route guard for authenticated pages

### Routing
```
/ → Redirect to /login
/login → LoginPage
/dashboard → Dashboard (Protected)
```

### Authentication Flow
1. User enters mobile number
2. Click "Send OTP"
3. OTP displayed (demo mode)
4. User enters OTP
5. Click "Verify OTP"
6. Token saved to localStorage
7. Redirect to /dashboard
8. Dashboard fetches user-specific data

### Logout Flow
1. Click "Logout" button
2. Token removed from localStorage
3. Redirect to /login

## Security Features

### OTP Security
- ✅ 6-digit random OTP
- ✅ 5-minute expiration
- ✅ One-time use (marked verified after use)
- ✅ Cannot reuse expired OTP

### JWT Security
- ✅ HS256 algorithm
- ✅ 24-hour token expiration
- ✅ User ID embedded in token
- ✅ Stateless authentication

### API Security
- ✅ All dashboard endpoints protected
- ✅ Auth endpoints public
- ✅ CORS configured
- ✅ 401 auto-logout on frontend

## Testing the Application

### Test Flow
1. Start backend: `cd backend && mvn spring-boot:run`
2. Start frontend: `cd frontend && npm run dev`
3. Open: `http://localhost:5173`
4. Enter mobile: `9876543210`
5. Click "Send OTP"
6. Copy OTP from alert (e.g., `123456`)
7. Enter OTP and verify
8. View personalized dashboard
9. Test logout

### Multiple Users
Each mobile number gets:
- Unique user account
- Separate fitness data
- Independent metrics
- Isolated alerts

Test with different numbers:
- `9876543210`
- `9876543211`
- `9876543212`

## Configuration

### Backend (`application.properties`)
```properties
# No changes needed - Spring Security auto-configured
```

### Frontend (`.env`)
```
VITE_API_BASE_URL=http://localhost:8080
```

## Migration from Old Version

### Database Migration
```sql
-- Drop old tables if needed
DROP TABLE IF EXISTS alerts;
DROP TABLE IF EXISTS heart_rate_log;
DROP TABLE IF EXISTS daily_metrics;
DROP TABLE IF EXISTS users;

-- Run new schema
SOURCE database/schema.sql;
```

### Code Changes
- ✅ User entity updated
- ✅ OTP entity added
- ✅ JWT dependencies added
- ✅ Security configuration added
- ✅ Service methods updated for user-specific data
- ✅ Controller methods extract userId from JWT
- ✅ Frontend routing added
- ✅ Login page created
- ✅ Protected routes implemented

## Troubleshooting

### Backend Issues

**Issue**: 401 Unauthorized on dashboard APIs
- **Solution**: Ensure token is in Authorization header

**Issue**: OTP not found
- **Solution**: Send OTP first before verifying

**Issue**: OTP expired
- **Solution**: Request new OTP (5-minute limit)

### Frontend Issues

**Issue**: Redirected to login after refresh
- **Solution**: Token expired (24 hours) - login again

**Issue**: CORS error
- **Solution**: Check backend CORS configuration allows `http://localhost:5173`

**Issue**: Can't see OTP
- **Solution**: Check blue info alert on login page (demo mode)

## Production Considerations

### For Production Deployment:

1. **Remove OTP from response**
   - Integrate real SMS gateway (Twilio, AWS SNS)
   - Remove `otp` field from SendOtpResponse

2. **Secure JWT secret**
   - Use environment variable for JWT key
   - Rotate keys periodically

3. **Rate limiting**
   - Limit OTP requests per mobile number
   - Prevent brute force attacks

4. **HTTPS only**
   - Enable HTTPS in production
   - Secure cookie storage

5. **Token refresh**
   - Implement refresh token mechanism
   - Extend session without re-login

## Summary

✅ Mobile number OTP authentication
✅ JWT-based security
✅ User-specific data isolation
✅ Protected routes
✅ Automatic data generation per user
✅ Logout functionality
✅ Token expiration handling
✅ Complete authentication flow
