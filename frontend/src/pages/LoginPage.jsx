import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Container, Box, Card, CardContent, TextField, Button, Typography, Alert } from '@mui/material';
import { Phone, Lock } from '@mui/icons-material';
import { authAPI } from '../services/api';

const LoginPage = () => {
  const navigate = useNavigate();
  const [mobileNumber, setMobileNumber] = useState('');
  const [otp, setOtp] = useState('');
  const [otpSent, setOtpSent] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [generatedOtp, setGeneratedOtp] = useState('');

  const handleSendOtp = async () => {
    if (mobileNumber.length !== 10) {
      setError('Please enter a valid 10-digit mobile number');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const response = await authAPI.sendOtp(mobileNumber);
      setOtpSent(true);
      setGeneratedOtp(response.data.otp);
      setError('');
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to send OTP');
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOtp = async () => {
    if (otp.length !== 6) {
      setError('Please enter a valid 6-digit OTP');
      return;
    }

    setLoading(true);
    setError('');
    try {
      const response = await authAPI.verifyOtp(mobileNumber, otp);
      localStorage.setItem('token', response.data.token);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.error || 'Invalid or expired OTP');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      }}
    >
      <Container maxWidth="sm">
        <Card sx={{ boxShadow: 6 }}>
          <CardContent sx={{ p: 4 }}>
            <Typography variant="h4" align="center" gutterBottom fontWeight="bold">
              Fitness Dashboard
            </Typography>
            <Typography variant="body2" align="center" color="textSecondary" sx={{ mb: 4 }}>
              Login with your mobile number
            </Typography>

            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error}
              </Alert>
            )}

            {generatedOtp && (
              <Alert severity="info" sx={{ mb: 2 }}>
                Demo OTP: {generatedOtp}
              </Alert>
            )}

            <TextField
              fullWidth
              label="Mobile Number"
              variant="outlined"
              value={mobileNumber}
              onChange={(e) => setMobileNumber(e.target.value.replace(/\D/g, '').slice(0, 10))}
              disabled={otpSent}
              InputProps={{
                startAdornment: <Phone sx={{ mr: 1, color: 'action.active' }} />,
              }}
              sx={{ mb: 2 }}
            />

            {!otpSent ? (
              <Button
                fullWidth
                variant="contained"
                size="large"
                onClick={handleSendOtp}
                disabled={loading}
                sx={{ py: 1.5 }}
              >
                {loading ? 'Sending...' : 'Send OTP'}
              </Button>
            ) : (
              <>
                <TextField
                  fullWidth
                  label="Enter OTP"
                  variant="outlined"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value.replace(/\D/g, '').slice(0, 6))}
                  InputProps={{
                    startAdornment: <Lock sx={{ mr: 1, color: 'action.active' }} />,
                  }}
                  sx={{ mb: 2 }}
                />
                <Button
                  fullWidth
                  variant="contained"
                  size="large"
                  onClick={handleVerifyOtp}
                  disabled={loading}
                  sx={{ py: 1.5, mb: 1 }}
                >
                  {loading ? 'Verifying...' : 'Verify OTP'}
                </Button>
                <Button
                  fullWidth
                  variant="text"
                  onClick={() => {
                    setOtpSent(false);
                    setOtp('');
                    setGeneratedOtp('');
                  }}
                >
                  Change Number
                </Button>
              </>
            )}
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
};

export default LoginPage;
