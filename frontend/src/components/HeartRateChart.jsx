import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

const HeartRateChart = ({ data }) => {
  return (
    <Card>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          24 Hour Heart Rate
        </Typography>
        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={data}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="time" />
            <YAxis domain={[50, 180]} />
            <Tooltip />
            <Line type="monotone" dataKey="heartRate" stroke="#ff6b6b" strokeWidth={2} dot={false} />
          </LineChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
};

export default HeartRateChart;
