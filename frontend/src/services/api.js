import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add JWT token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle 401 errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  sendOtp: (mobileNumber) => api.post('/api/auth/send-otp', { mobileNumber }),
  verifyOtp: (mobileNumber, otp) => api.post('/api/auth/verify-otp', { mobileNumber, otp }),
};

export const dashboardAPI = {
  getSummary: () => api.get('/api/dashboard/summary'),
  getWeeklyMetrics: () => api.get('/api/dashboard/weekly'),
  getHeartRate: () => api.get('/api/dashboard/heartrate'),
  getAlerts: () => api.get('/api/dashboard/alerts'),
  getSleepBreakdown: () => api.get('/api/dashboard/sleep'),
};
