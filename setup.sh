#!/bin/bash

echo "=========================================="
echo "Wearable Fitness Dashboard Setup"
echo "=========================================="

# Check prerequisites
echo "Checking prerequisites..."

if ! command -v java &> /dev/null; then
    echo "❌ Java not found. Please install Java 17+"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo "❌ Maven not found. Please install Maven 3.6+"
    exit 1
fi

if ! command -v node &> /dev/null; then
    echo "❌ Node.js not found. Please install Node.js 18+"
    exit 1
fi

if ! command -v mysql &> /dev/null; then
    echo "⚠️  MySQL command not found. Make sure MySQL server is running."
fi

echo "✅ Prerequisites check complete"
echo ""

# Setup backend
echo "Setting up backend..."
cd backend
mvn clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Backend build failed"
    exit 1
fi
echo "✅ Backend setup complete"
echo ""

# Setup frontend
echo "Setting up frontend..."
cd ../frontend
npm install
if [ $? -ne 0 ]; then
    echo "❌ Frontend setup failed"
    exit 1
fi
echo "✅ Frontend setup complete"
echo ""

echo "=========================================="
echo "Setup Complete!"
echo "=========================================="
echo ""
echo "To run the application:"
echo "1. Start MySQL server"
echo "2. Backend: cd backend && mvn spring-boot:run"
echo "3. Frontend: cd frontend && npm run dev"
echo "4. Open http://localhost:5173"
echo ""
