import React from 'react';
import { Card, CardContent, Typography, Box } from '@mui/material';

const SummaryCard = ({ title, value, unit, icon, color }) => {
  return (
    <Card sx={{ height: '100%', background: `linear-gradient(135deg, ${color}15 0%, ${color}05 100%)` }}>
      <CardContent>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Box>
            <Typography color="textSecondary" gutterBottom variant="body2">
              {title}
            </Typography>
            <Typography variant="h4" component="div" sx={{ color, fontWeight: 'bold' }}>
              {value}
            </Typography>
            <Typography variant="body2" color="textSecondary">
              {unit}
            </Typography>
          </Box>
          <Box sx={{ fontSize: 40, color, opacity: 0.3 }}>
            {icon}
          </Box>
        </Box>
      </CardContent>
    </Card>
  );
};

export default SummaryCard;
