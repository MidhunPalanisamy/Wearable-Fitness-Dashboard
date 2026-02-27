# Architecture & Flow Diagrams

## System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        FRONTEND (React)                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  LoginPage   │  │ ProtectedRoute│  │  Dashboard   │      │
│  │              │  │               │  │              │      │
│  │ - Mobile     │  │ - Check Token │  │ - Charts     │      │
│  │ - OTP Input  │  │ - Redirect    │  │ - Metrics    │      │
│  │ - Verify     │  │               │  │ - Alerts     │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│         │                  │                  │              │
│         └──────────────────┴──────────────────┘              │
│                            │                                 │
│                    ┌───────▼────────┐                        │
│                    │   API Service   │                       │
│                    │  (Axios + JWT)  │                       │
│                    └───────┬────────┘                        │
└────────────────────────────┼──────────────────────────────────┘
                             │
                    HTTP + JWT Token
                             │
┌────────────────────────────▼──────────────────────────────────┐
│                     BACKEND (Spring Boot)                      │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │              Security Layer (Spring Security)             │ │
│  │  ┌────────────────────┐  ┌──────────────────────┐        │ │
│  │  │ JwtAuthFilter      │  │  SecurityConfig      │        │ │
│  │  │ - Validate Token   │  │  - CORS              │        │ │
│  │  │ - Extract User ID  │  │  - Public/Protected  │        │ │
│  │  └────────────────────┘  └──────────────────────┘        │ │
│  └──────────────────────────────────────────────────────────┘ │
│                             │                                  │
│  ┌──────────────────────────▼──────────────────────────────┐  │
│  │                    Controllers                           │  │
│  │  ┌──────────────────┐  ┌──────────────────────────┐    │  │
│  │  │  AuthController  │  │  DashboardController     │    │  │
│  │  │  - /send-otp     │  │  - /summary              │    │  │
│  │  │  - /verify-otp   │  │  - /weekly               │    │  │
│  │  └──────────────────┘  │  - /heartrate            │    │  │
│  │                        │  - /alerts               │    │  │
│  │                        │  - /sleep                │    │  │
│  │                        └──────────────────────────┘    │  │
│  └──────────────────────────┬──────────────────────────────┘  │
│                             │                                  │
│  ┌──────────────────────────▼──────────────────────────────┐  │
│  │                      Services                            │  │
│  │  ┌──────────────────┐  ┌──────────────────────────┐    │  │
│  │  │   AuthService    │  │   DashboardService       │    │  │
│  │  │  - Generate OTP  │  │  - Generate User Data    │    │  │
│  │  │  - Verify OTP    │  │  - Get Metrics           │    │  │
│  │  │  - Create User   │  │  - Get Alerts            │    │  │
│  │  └──────────────────┘  └──────────────────────────┘    │  │
│  └──────────────────────────┬──────────────────────────────┘  │
│                             │                                  │
│  ┌──────────────────────────▼──────────────────────────────┐  │
│  │                    Repositories                          │  │
│  │  UserRepo │ OTPRepo │ MetricsRepo │ HeartRateRepo │     │  │
│  │  AlertRepo                                               │  │
│  └──────────────────────────┬──────────────────────────────┘  │
└─────────────────────────────┼─────────────────────────────────┘
                              │
                    ┌─────────▼─────────┐
                    │   MySQL Database   │
                    │  - users           │
                    │  - otp             │
                    │  - daily_metrics   │
                    │  - heart_rate_log  │
                    │  - alerts          │
                    └────────────────────┘
```

## Authentication Flow

```
┌─────────┐                                              ┌─────────┐
│ Browser │                                              │ Backend │
└────┬────┘                                              └────┬────┘
     │                                                        │
     │  1. Enter Mobile Number (9876543210)                  │
     ├───────────────────────────────────────────────────────>
     │     POST /api/auth/send-otp                           │
     │     { "mobileNumber": "9876543210" }                  │
     │                                                        │
     │                                    2. Check if user exists
     │                                    3. Create user if new
     │                                    4. Generate 6-digit OTP
     │                                    5. Save OTP (5 min expiry)
     │                                                        │
     │  6. Return OTP (demo mode)                            │
     <───────────────────────────────────────────────────────┤
     │     { "message": "OTP sent", "otp": "123456" }        │
     │                                                        │
     │  7. User enters OTP                                   │
     │                                                        │
     │  8. Verify OTP                                        │
     ├───────────────────────────────────────────────────────>
     │     POST /api/auth/verify-otp                         │
     │     { "mobileNumber": "9876543210", "otp": "123456" } │
     │                                                        │
     │                                    9. Validate OTP
     │                                    10. Check expiry
     │                                    11. Mark verified
     │                                    12. Generate JWT token
     │                                    13. Generate user data
     │                                                        │
     │  14. Return JWT token                                 │
     <───────────────────────────────────────────────────────┤
     │     { "token": "eyJhbG...", "message": "Success" }    │
     │                                                        │
     │  15. Store token in localStorage                      │
     │  16. Redirect to /dashboard                           │
     │                                                        │
     │  17. Request dashboard data                           │
     ├───────────────────────────────────────────────────────>
     │     GET /api/dashboard/summary                        │
     │     Authorization: Bearer eyJhbG...                   │
     │                                                        │
     │                                    18. Validate JWT
     │                                    19. Extract user ID
     │                                    20. Fetch user data
     │                                                        │
     │  21. Return user-specific data                        │
     <───────────────────────────────────────────────────────┤
     │     { "steps": 8500, "calories": 2100, ... }          │
     │                                                        │
     │  22. Display dashboard                                │
     │                                                        │
```

## Data Isolation Flow

```
┌──────────────────────────────────────────────────────────────┐
│                    User Registration                          │
└──────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │  Mobile Number  │
                    │   9876543210    │
                    └────────┬────────┘
                             │
                    ┌────────▼────────┐
                    │  Create User    │
                    │  user_id: 1     │
                    └────────┬────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│ Daily Metrics │   │ Heart Rate    │   │    Alerts     │
│  (30 days)    │   │  (24 hours)   │   │  (Generated)  │
│               │   │               │   │               │
│ user_id: 1    │   │ user_id: 1    │   │ user_id: 1    │
│ date: today   │   │ timestamp: now│   │ type: High HR │
│ steps: 8500   │   │ hr: 75        │   │ severity: HIGH│
└───────────────┘   └───────────────┘   └───────────────┘

┌──────────────────────────────────────────────────────────────┐
│              Different User = Different Data                  │
└──────────────────────────────────────────────────────────────┘

User 1 (9876543210)          User 2 (9876543211)
     │                            │
     ├─ Steps: 8500               ├─ Steps: 12000
     ├─ Calories: 2100            ├─ Calories: 2800
     ├─ HR: 75                    ├─ HR: 82
     └─ Sleep: 7.5h               └─ Sleep: 6.2h
```

## JWT Token Structure

```
┌─────────────────────────────────────────────────────────────┐
│                      JWT Token                               │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Header                                                      │
│  ┌────────────────────────────────────────────────────┐     │
│  │ {                                                   │     │
│  │   "alg": "HS256",                                  │     │
│  │   "typ": "JWT"                                     │     │
│  │ }                                                   │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│  Payload                                                     │
│  ┌────────────────────────────────────────────────────┐     │
│  │ {                                                   │     │
│  │   "sub": "1",              // User ID              │     │
│  │   "mobileNumber": "9876543210",                    │     │
│  │   "iat": 1708963200,       // Issued at           │     │
│  │   "exp": 1709049600        // Expires (24h)       │     │
│  │ }                                                   │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
│  Signature                                                   │
│  ┌────────────────────────────────────────────────────┐     │
│  │ HMACSHA256(                                         │     │
│  │   base64UrlEncode(header) + "." +                  │     │
│  │   base64UrlEncode(payload),                        │     │
│  │   secret                                            │     │
│  │ )                                                   │     │
│  └────────────────────────────────────────────────────┘     │
│                                                              │
└─────────────────────────────────────────────────────────────┘

Result: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwi...
```

## Request/Response Flow

```
┌─────────────────────────────────────────────────────────────┐
│                  Protected API Request                       │
└─────────────────────────────────────────────────────────────┘

Browser                    Filter                   Controller
   │                         │                          │
   │  GET /api/dashboard/summary                        │
   │  Authorization: Bearer token                       │
   ├────────────────────────>│                          │
   │                         │                          │
   │                    Extract token                   │
   │                    Validate signature              │
   │                    Check expiration                │
   │                    Extract user ID                 │
   │                         │                          │
   │                         │  Authentication(userId)  │
   │                         ├─────────────────────────>│
   │                         │                          │
   │                         │              Get user data
   │                         │              from service
   │                         │                          │
   │                         │  User-specific response  │
   │                         │<─────────────────────────┤
   │                         │                          │
   │  { "steps": 8500, ... }                            │
   │<────────────────────────┤                          │
   │                         │                          │
```

## Database Relationships

```
┌─────────────────┐
│     users       │
│─────────────────│
│ id (PK)         │◄──────────┐
│ name            │           │
│ mobile_number   │           │
│ created_at      │           │
└─────────────────┘           │
                              │
                              │ user_id (FK)
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        │                     │                     │
┌───────▼────────┐   ┌────────▼───────┐   ┌────────▼───────┐
│ daily_metrics  │   │ heart_rate_log │   │    alerts      │
│────────────────│   │────────────────│   │────────────────│
│ id (PK)        │   │ id (PK)        │   │ id (PK)        │
│ date           │   │ timestamp      │   │ type           │
│ steps          │   │ heart_rate     │   │ message        │
│ calories       │   │ user_id (FK)   │   │ severity       │
│ sleep_hours    │   └────────────────┘   │ timestamp      │
│ avg_heart_rate │                        │ user_id (FK)   │
│ user_id (FK)   │                        └────────────────┘
└────────────────┘

┌─────────────────┐
│      otp        │
│─────────────────│
│ id (PK)         │
│ mobile_number   │
│ otp_code        │
│ expiry_time     │
│ verified        │
└─────────────────┘
```

## Component Hierarchy

```
App.jsx
│
├── BrowserRouter
│   │
│   ├── Route: /login
│   │   └── LoginPage
│   │       ├── Mobile Input
│   │       ├── Send OTP Button
│   │       ├── OTP Input
│   │       └── Verify Button
│   │
│   ├── Route: /dashboard (Protected)
│   │   └── ProtectedRoute
│   │       └── Dashboard
│   │           ├── AppBar
│   │           │   ├── Title
│   │           │   ├── Refresh Button
│   │           │   └── Logout Button
│   │           │
│   │           └── Container
│   │               ├── SummaryCard (Steps)
│   │               ├── SummaryCard (Calories)
│   │               ├── SummaryCard (Heart Rate)
│   │               ├── SummaryCard (Sleep)
│   │               ├── WeeklyStepsChart
│   │               ├── HeartRateChart
│   │               ├── SleepChart
│   │               └── AlertsPanel
│   │
│   └── Route: / (Redirect to /login)
```

## Security Layers

```
┌─────────────────────────────────────────────────────────────┐
│                     Security Layers                          │
└─────────────────────────────────────────────────────────────┘

Layer 1: Frontend Route Protection
┌────────────────────────────────────────┐
│ ProtectedRoute Component               │
│ - Check localStorage for token         │
│ - Redirect to /login if missing        │
└────────────────────────────────────────┘

Layer 2: Axios Interceptor
┌────────────────────────────────────────┐
│ Request Interceptor                    │
│ - Add Authorization header             │
│ - Include JWT token                    │
└────────────────────────────────────────┘
┌────────────────────────────────────────┐
│ Response Interceptor                   │
│ - Catch 401 errors                     │
│ - Clear token                          │
│ - Redirect to login                    │
└────────────────────────────────────────┘

Layer 3: Spring Security Filter
┌────────────────────────────────────────┐
│ JwtAuthenticationFilter                │
│ - Extract token from header            │
│ - Validate token signature             │
│ - Check expiration                     │
│ - Set authentication context           │
└────────────────────────────────────────┘

Layer 4: Security Configuration
┌────────────────────────────────────────┐
│ SecurityConfig                         │
│ - Public: /api/auth/**                 │
│ - Protected: /api/dashboard/**         │
│ - CORS configuration                   │
│ - Stateless session                    │
└────────────────────────────────────────┘

Layer 5: Controller Authorization
┌────────────────────────────────────────┐
│ DashboardController                    │
│ - Extract user ID from Authentication  │
│ - Pass to service layer                │
└────────────────────────────────────────┘

Layer 6: Data Isolation
┌────────────────────────────────────────┐
│ DashboardService                       │
│ - Query by user ID                     │
│ - Return only user's data              │
└────────────────────────────────────────┘
```

This architecture ensures:
- ✅ Secure authentication
- ✅ Token-based authorization
- ✅ User data isolation
- ✅ Protected endpoints
- ✅ Automatic session management
