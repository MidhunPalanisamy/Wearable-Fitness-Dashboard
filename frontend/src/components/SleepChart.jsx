import React from 'react';
import { Card, CardContent, Typography } from '@mui/material';
import { PieChart, Pie, Cell, ResponsiveContainer, Legend, Tooltip } from 'recharts';

const SleepChart = ({ data }) => {
  const chartData = [
    { name: 'Deep Sleep', value: data.deepSleep, color: '#4c51bf' },
    { name: 'Light Sleep', value: data.lightSleep, color: '#48bb78' },
    { name: 'REM Sleep', value: data.remSleep, color: '#ed8936' },
  ];

  return (
    <Card>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Sleep Breakdown
        </Typography>
        <ResponsiveContainer width="100%" height={300}>
          <PieChart>
            <Pie
              data={chartData}
              cx="50%"
              cy="50%"
              labelLine={false}
              label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
              outerRadius={80}
              fill="#8884d8"
              dataKey="value"
            >
              {chartData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry.color} />
              ))}
            </Pie>
            <Tooltip />
            <Legend />
          </PieChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
};

export default SleepChart;
