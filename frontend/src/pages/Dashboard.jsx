import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Grid, AppBar, Toolbar, Typography, Button, CircularProgress, Box } from '@mui/material';
import { Refresh, DirectionsRun, Hotel, Favorite, LocalFireDepartment, Logout } from '@mui/icons-material';
import SummaryCard from '../components/SummaryCard';
import WeeklyStepsChart from '../components/WeeklyStepsChart';
import HeartRateChart from '../components/HeartRateChart';
import SleepChart from '../components/SleepChart';
import AlertsPanel from '../components/AlertsPanel';
import { dashboardAPI } from '../services/api';

const Dashboard = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [summary, setSummary] = useState({ steps: 0, calories: 0, avgHeartRate: 0, sleepHours: 0 });
  const [weeklyData, setWeeklyData] = useState([]);
  const [heartRateData, setHeartRateData] = useState([]);
  const [sleepData, setSleepData] = useState({ deepSleep: 0, lightSleep: 0, remSleep: 0 });
  const [alerts, setAlerts] = useState([]);
  const [error, setError] = useState(null);

  const fetchData = async () => {
    setLoading(true);
    setError(null);
    try {
      const [summaryRes, weeklyRes, heartRateRes, sleepRes, alertsRes] = await Promise.all([
        dashboardAPI.getSummary(),
        dashboardAPI.getWeeklyMetrics(),
        dashboardAPI.getHeartRate(),
        dashboardAPI.getSleepBreakdown(),
        dashboardAPI.getAlerts(),
      ]);

      setSummary(summaryRes.data);
      setWeeklyData(weeklyRes.data.reverse());
      setHeartRateData(heartRateRes.data);
      setSleepData(sleepRes.data);
      setAlerts(alertsRes.data);
    } catch (err) {
      setError('Failed to fetch dashboard data. Please ensure the backend is running.');
      console.error('Error fetching data:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  useEffect(() => {
    fetchData();
  }, []);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <CircularProgress size={60} />
      </Box>
    );
  }

  if (error) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <Typography color="error" variant="h6">{error}</Typography>
      </Box>
    );
  }

  return (
    <>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Wearable Fitness Dashboard
          </Typography>
          <Button color="inherit" startIcon={<Refresh />} onClick={fetchData} sx={{ mr: 1 }}>
            Refresh
          </Button>
          <Button color="inherit" startIcon={<Logout />} onClick={handleLogout}>
            Logout
          </Button>
        </Toolbar>
      </AppBar>

      <Container maxWidth="xl" sx={{ mt: 4, mb: 4 }}>
        <Grid container spacing={3}>
          {/* Summary Cards */}
          <Grid item xs={12} sm={6} md={3}>
            <SummaryCard
              title="Steps"
              value={summary.steps.toLocaleString()}
              unit="steps"
              icon={<DirectionsRun />}
              color="#3f51b5"
            />
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <SummaryCard
              title="Calories"
              value={summary.calories.toLocaleString()}
              unit="kcal"
              icon={<LocalFireDepartment />}
              color="#ff9800"
            />
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <SummaryCard
              title="Heart Rate"
              value={summary.avgHeartRate}
              unit="bpm"
              icon={<Favorite />}
              color="#f44336"
            />
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <SummaryCard
              title="Sleep"
              value={summary.sleepHours.toFixed(1)}
              unit="hours"
              icon={<Hotel />}
              color="#9c27b0"
            />
          </Grid>

          {/* Charts */}
          <Grid item xs={12} md={6}>
            <WeeklyStepsChart data={weeklyData} />
          </Grid>
          <Grid item xs={12} md={6}>
            <HeartRateChart data={heartRateData} />
          </Grid>
          <Grid item xs={12} md={6}>
            <SleepChart data={sleepData} />
          </Grid>
          <Grid item xs={12} md={6}>
            <AlertsPanel alerts={alerts} />
          </Grid>
        </Grid>
      </Container>
    </>
  );
};

export default Dashboard;
