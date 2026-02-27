import React from 'react';
import { Card, CardContent, Typography, List, ListItem, ListItemText, Chip, Box } from '@mui/material';

const AlertsPanel = ({ alerts }) => {
  const getSeverityColor = (severity) => {
    switch (severity) {
      case 'HIGH': return 'error';
      case 'MEDIUM': return 'warning';
      case 'LOW': return 'info';
      default: return 'default';
    }
  };

  return (
    <Card>
      <CardContent>
        <Typography variant="h6" gutterBottom>
          Recent Alerts
        </Typography>
        <List>
          {alerts.map((alert, index) => (
            <ListItem key={index} divider>
              <ListItemText
                primary={
                  <Box display="flex" alignItems="center" gap={1}>
                    <Typography variant="body1">{alert.type}</Typography>
                    <Chip label={alert.severity} color={getSeverityColor(alert.severity)} size="small" />
                  </Box>
                }
                secondary={
                  <>
                    <Typography variant="body2" color="textSecondary">
                      {alert.message}
                    </Typography>
                    <Typography variant="caption" color="textSecondary">
                      {alert.timestamp}
                    </Typography>
                  </>
                }
              />
            </ListItem>
          ))}
        </List>
      </CardContent>
    </Card>
  );
};

export default AlertsPanel;
