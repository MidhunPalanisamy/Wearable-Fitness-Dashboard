# Wearable Fitness Dashboard

A full-stack fitness tracking application with Spring Boot backend and React frontend, featuring mobile number OTP authentication and user-specific data.

## Tech Stack

### Backend
- Spring Boot 3
- Java 17
- MySQL
- Spring Data JPA
- Spring Security
- JWT Authentication
- Faker library for data generation

### Frontend
- React 18
- Vite
- React Router
- Material UI
- Recharts
- Axios

## Features

### Authentication
- 📱 Mobile number login
- 🔐 OTP verification (6-digit, 5-minute expiry)
- 🎫 JWT token-based authentication
- 🔒 Protected routes
- 👤 User-specific data isolation

### Dashboard
- 📊 Real-time fitness metrics
- 📈 Weekly activity charts
- ❤️ 24-hour heart rate monitoring
- 😴 Sleep breakdown analysis
- ⚠️ Health alerts
- 🔄 Auto-refresh data

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 18+
- MySQL 8.0+

### 1. Database Setup

```bash
mysql -u root -p
```

```sql
CREATE DATABASE fitness_dashboard;
USE fitness_dashboard;
SOURCE database/schema.sql;
```

Update credentials in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### 2. Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend runs on: `http://localhost:8080`

### 3. Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on: `http://localhost:5173`

## Usage

### Login Flow

1. Open `http://localhost:5173`
2. Enter mobile number (e.g., `9876543210`)
3. Click "Send OTP"
4. Copy OTP from blue alert (demo mode)
5. Enter OTP and click "Verify"
6. Access personalized dashboard

### Dashboard Features

- **Summary Cards**: Steps, Calories, Heart Rate, Sleep
- **Weekly Chart**: 7-day step history
- **Heart Rate**: 24-hour monitoring
- **Sleep Analysis**: Deep/Light/REM breakdown
- **Alerts**: Health notifications
- **Refresh**: Update all data
- **Logout**: End session

## API Documentation

### Authentication APIs

#### Send OTP
```http
POST /api/auth/send-otp
Content-Type: application/json

{
  "mobileNumber": "9876543210"
}
```

Response:
```json
{
  "message": "OTP sent successfully",
  "otp": "123456"
}
```

#### Verify OTP
```http
POST /api/auth/verify-otp
Content-Type: application/json

{
  "mobileNumber": "9876543210",
  "otp": "123456"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "message": "Login successful"
}
```

### Dashboard APIs (Protected)

All require: `Authorization: Bearer <token>`

- `GET /api/dashboard/summary` - Today's metrics
- `GET /api/dashboard/weekly` - 7-day history
- `GET /api/dashboard/heartrate` - 24-hour heart rate
- `GET /api/dashboard/alerts` - Recent alerts
- `GET /api/dashboard/sleep` - Sleep breakdown

## Project Structure

```
Wearable Fitness Dashboard/
├── backend/
│   ├── src/main/java/com/fitness/dashboard/
│   │   ├── auth/              # Authentication logic
│   │   ├── security/          # JWT & Security config
│   │   ├── util/              # JWT utility
│   │   ├── entity/            # JPA entities
│   │   ├── repository/        # Data repositories
│   │   ├── service/           # Business logic
│   │   ├── controller/        # REST controllers
│   │   └── dto/               # Data transfer objects
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── frontend/
│   ├── src/
│   │   ├── components/        # React components
│   │   ├── pages/             # Login & Dashboard
│   │   ├── services/          # API client
│   │   ├── App.jsx            # Router setup
│   │   └── main.jsx
│   ├── package.json
│   └── .env
├── database/
│   └── schema.sql
├── README.md
└── AUTHENTICATION_GUIDE.md
```

## Security Features

### OTP Security
- ✅ 6-digit random OTP
- ✅ 5-minute expiration
- ✅ One-time use only
- ✅ Secure storage

### JWT Security
- ✅ HS256 encryption
- ✅ 24-hour token validity
- ✅ User ID embedded
- ✅ Stateless authentication

### API Security
- ✅ Protected endpoints
- ✅ CORS configured
- ✅ Auto-logout on 401
- ✅ Token validation

## Data Generation

Automatic per-user data generation on first login:

- **30 days** of daily metrics
- **144 logs** of heart rate (every 10 min)
- **Steps**: 3,000-15,000
- **Calories**: 1,500-3,000
- **Heart Rate**: 60-100 (normal), 120-160 (workout)
- **Sleep**: 5-9 hours (30% deep, 50% light, 20% REM)

### Alert Rules
- High Heart Rate: Avg > 95 bpm (HIGH)
- Low Sleep: < 6 hours (MEDIUM)
- Low Activity: < 4,000 steps (LOW)

## Testing Multiple Users

Each mobile number creates a separate user with unique data:

```
User 1: 9876543210
User 2: 9876543211
User 3: 9876543212
```

Each user has:
- Isolated fitness data
- Independent metrics
- Separate alerts
- Unique dashboard

## Environment Variables

### Frontend `.env`
```
VITE_API_BASE_URL=http://localhost:8080
```

### Backend `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fitness_dashboard
spring.datasource.username=root
spring.datasource.password=root
server.port=8080
```

## Build for Production

### Backend
```bash
cd backend
mvn clean package
java -jar target/dashboard-1.0.0.jar
```

### Frontend
```bash
cd frontend
npm run build
# Deploy dist/ folder
```

## Troubleshooting

### Backend Issues

**MySQL Connection Error**
- Verify MySQL is running
- Check credentials in `application.properties`
- Ensure database exists

**Port 8080 in use**
```bash
lsof -ti:8080 | xargs kill -9
```

### Frontend Issues

**401 Unauthorized**
- Token expired (24 hours) - login again
- Token missing - check localStorage

**CORS Error**
- Verify backend allows `http://localhost:5173`
- Check SecurityConfig.java

**Can't see OTP**
- Look for blue info alert on login page
- OTP shown in demo mode only

## Production Deployment

### Security Enhancements

1. **SMS Integration**
   - Replace demo OTP with real SMS (Twilio/AWS SNS)
   - Remove OTP from API response

2. **JWT Secret**
   - Use environment variable
   - Rotate keys regularly

3. **Rate Limiting**
   - Limit OTP requests per number
   - Prevent brute force

4. **HTTPS**
   - Enable SSL/TLS
   - Secure cookies

5. **Token Refresh**
   - Implement refresh tokens
   - Extend sessions

## Documentation

- [README.md](README.md)
- [AUTHENTICATION_GUIDE.md](AUTHENTICATION_GUIDE.md) 
- [PROJECT_SUMMARY.md](PROJECT_SUMMARY.md) 

## License

MIT

## Support

For issues or questions:
1. Check AUTHENTICATION_GUIDE.md
2. Review troubleshooting section
3. Verify all prerequisites installed
