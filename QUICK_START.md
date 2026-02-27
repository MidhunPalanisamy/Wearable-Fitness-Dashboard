# Quick Start Guide - Wearable Fitness Dashboard

## 🚀 Get Started in 5 Minutes

### Step 1: Database Setup (1 minute)

```bash
# Start MySQL
mysql -u root -p

# Create database and tables
CREATE DATABASE fitness_dashboard;
USE fitness_dashboard;
SOURCE database/schema.sql;
exit;
```

### Step 2: Backend Setup (2 minutes)

```bash
# Navigate to backend
cd backend

# Update MySQL password in src/main/resources/application.properties
# Change: spring.datasource.password=your_password

# Build and run
mvn clean install
mvn spring-boot:run
```

✅ Backend running on: `http://localhost:8080`

### Step 3: Frontend Setup (2 minutes)

```bash
# Open new terminal
cd frontend

# Install and run
npm install
npm run dev
```

✅ Frontend running on: `http://localhost:5173`

### Step 4: Login & Test

1. Open browser: `http://localhost:5173`
2. Enter mobile: `9876543210`
3. Click "Send OTP"
4. Copy OTP from blue alert (e.g., `123456`)
5. Enter OTP and click "Verify"
6. ✅ Dashboard loaded with your data!

---

## 📱 Test Multiple Users

Try these mobile numbers:
- `9876543210`
- `9876543211`
- `9876543212`

Each gets unique data!

---

## 🔧 Troubleshooting

### Backend won't start
```bash
# Check Java version
java -version  # Should be 17+

# Check MySQL
mysql -u root -p -e "SHOW DATABASES;"

# Check port
lsof -ti:8080 | xargs kill -9
```

### Frontend won't start
```bash
# Clear cache
rm -rf node_modules package-lock.json
npm install

# Check port
lsof -ti:5173 | xargs kill -9
```

### Can't login
- Check backend is running: `http://localhost:8080`
- Check browser console for errors
- Verify MySQL database exists

---

## 📚 Documentation

- **README.md** - Full documentation
- **AUTHENTICATION_GUIDE.md** - Auth details
- **API_TESTING.md** - API examples
- **UPGRADE_SUMMARY.md** - What's new

---

## ✅ Verification

Test these:
- [ ] Login with mobile number
- [ ] Receive OTP
- [ ] Verify OTP
- [ ] View dashboard
- [ ] Refresh data
- [ ] Logout
- [ ] Login with different number

---

## 🎉 You're Ready!

Your fitness dashboard is running with:
- ✅ Secure authentication
- ✅ User-specific data
- ✅ Real-time charts
- ✅ Health alerts

**Enjoy!** 🚀
