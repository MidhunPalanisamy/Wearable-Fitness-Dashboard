# API Testing Examples

## Using cURL

### 1. Send OTP

```bash
curl -X POST http://localhost:8080/api/auth/send-otp \
  -H "Content-Type: application/json" \
  -d '{"mobileNumber":"9876543210"}'
```

Response:
```json
{
  "message": "OTP sent successfully",
  "otp": "123456"
}
```

### 2. Verify OTP

```bash
curl -X POST http://localhost:8080/api/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d '{"mobileNumber":"9876543210","otp":"123456"}'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibW9iaWxlTnVtYmVyIjoiOTg3NjU0MzIxMCIsImlhdCI6MTcwODk2MzIwMCwiZXhwIjoxNzA5MDQ5NjAwfQ.xyz...",
  "message": "Login successful"
}
```

### 3. Get Dashboard Summary (Protected)

```bash
TOKEN="your_jwt_token_here"

curl -X GET http://localhost:8080/api/dashboard/summary \
  -H "Authorization: Bearer $TOKEN"
```

Response:
```json
{
  "steps": 8500,
  "calories": 2100,
  "avgHeartRate": 75,
  "sleepHours": 7.5
}
```

### 4. Get Weekly Metrics

```bash
curl -X GET http://localhost:8080/api/dashboard/weekly \
  -H "Authorization: Bearer $TOKEN"
```

Response:
```json
[
  {
    "date": "02/20",
    "steps": 8500,
    "calories": 2100
  },
  {
    "date": "02/21",
    "steps": 9200,
    "calories": 2300
  }
]
```

### 5. Get Heart Rate Logs

```bash
curl -X GET http://localhost:8080/api/dashboard/heartrate \
  -H "Authorization: Bearer $TOKEN"
```

Response:
```json
[
  {
    "time": "14:30",
    "heartRate": 75
  },
  {
    "time": "14:40",
    "heartRate": 78
  }
]
```

### 6. Get Alerts

```bash
curl -X GET http://localhost:8080/api/dashboard/alerts \
  -H "Authorization: Bearer $TOKEN"
```

Response:
```json
[
  {
    "type": "High Heart Rate",
    "message": "Average heart rate exceeded 95 bpm",
    "severity": "HIGH",
    "timestamp": "2024-02-20 12:00"
  },
  {
    "type": "Low Sleep",
    "message": "Sleep duration below 6 hours",
    "severity": "MEDIUM",
    "timestamp": "2024-02-20 08:00"
  }
]
```

### 7. Get Sleep Breakdown

```bash
curl -X GET http://localhost:8080/api/dashboard/sleep \
  -H "Authorization: Bearer $TOKEN"
```

Response:
```json
{
  "deepSleep": 2.3,
  "lightSleep": 3.8,
  "remSleep": 1.5
}
```

## Using Postman

### Setup

1. Create new collection: "Fitness Dashboard"
2. Add environment variable: `baseUrl` = `http://localhost:8080`
3. Add environment variable: `token` = (will be set after login)

### Request 1: Send OTP

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/send-otp`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "mobileNumber": "9876543210"
}
```

### Request 2: Verify OTP

- Method: `POST`
- URL: `{{baseUrl}}/api/auth/verify-otp`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "mobileNumber": "9876543210",
  "otp": "123456"
}
```

**Test Script** (to save token):
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("token", jsonData.token);
}
```

### Request 3: Get Summary

- Method: `GET`
- URL: `{{baseUrl}}/api/dashboard/summary`
- Headers: `Authorization: Bearer {{token}}`

### Request 4-7: Other Dashboard Endpoints

Same pattern:
- Method: `GET`
- URL: `{{baseUrl}}/api/dashboard/{endpoint}`
- Headers: `Authorization: Bearer {{token}}`

## Error Responses

### 400 Bad Request (Invalid Mobile)
```json
{
  "error": "Invalid mobile number"
}
```

### 401 Unauthorized (Invalid OTP)
```json
{
  "error": "Invalid OTP"
}
```

### 401 Unauthorized (Expired OTP)
```json
{
  "error": "OTP expired"
}
```

### 401 Unauthorized (No Token)
```json
{
  "timestamp": "2024-02-26T10:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "path": "/api/dashboard/summary"
}
```

## Complete Test Flow

```bash
#!/bin/bash

# 1. Send OTP
echo "Sending OTP..."
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/send-otp \
  -H "Content-Type: application/json" \
  -d '{"mobileNumber":"9876543210"}')

echo "Response: $RESPONSE"

# Extract OTP (requires jq)
OTP=$(echo $RESPONSE | jq -r '.otp')
echo "OTP: $OTP"

# 2. Verify OTP
echo "Verifying OTP..."
TOKEN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/verify-otp \
  -H "Content-Type: application/json" \
  -d "{\"mobileNumber\":\"9876543210\",\"otp\":\"$OTP\"}")

echo "Token Response: $TOKEN_RESPONSE"

# Extract token
TOKEN=$(echo $TOKEN_RESPONSE | jq -r '.token')
echo "Token: $TOKEN"

# 3. Get Dashboard Data
echo "Fetching dashboard summary..."
curl -X GET http://localhost:8080/api/dashboard/summary \
  -H "Authorization: Bearer $TOKEN"

echo "\n\nFetching weekly metrics..."
curl -X GET http://localhost:8080/api/dashboard/weekly \
  -H "Authorization: Bearer $TOKEN"

echo "\n\nFetching alerts..."
curl -X GET http://localhost:8080/api/dashboard/alerts \
  -H "Authorization: Bearer $TOKEN"
```

Save as `test-api.sh` and run:
```bash
chmod +x test-api.sh
./test-api.sh
```

## Testing Multiple Users

```bash
# User 1
curl -X POST http://localhost:8080/api/auth/send-otp \
  -H "Content-Type: application/json" \
  -d '{"mobileNumber":"9876543210"}'

# User 2
curl -X POST http://localhost:8080/api/auth/send-otp \
  -H "Content-Type: application/json" \
  -d '{"mobileNumber":"9876543211"}'

# User 3
curl -X POST http://localhost:8080/api/auth/send-otp \
  -H "Content-Type: application/json" \
  -d '{"mobileNumber":"9876543212"}'
```

Each user will have:
- Different JWT token
- Separate fitness data
- Unique dashboard metrics
