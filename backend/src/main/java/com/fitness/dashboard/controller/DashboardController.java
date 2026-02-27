package com.fitness.dashboard.controller;

import com.fitness.dashboard.dto.*;
import com.fitness.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/summary")
    public SummaryDTO getSummary(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return dashboardService.getTodaySummary(userId);
    }
    
    @GetMapping("/weekly")
    public List<WeeklyMetricsDTO> getWeeklyMetrics(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return dashboardService.getWeeklyMetrics(userId);
    }
    
    @GetMapping("/heartrate")
    public List<HeartRateDTO> getHeartRate(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return dashboardService.getHeartRateLogs(userId);
    }
    
    @GetMapping("/alerts")
    public List<AlertDTO> getAlerts(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return dashboardService.getAlerts(userId);
    }
    
    @GetMapping("/sleep")
    public SleepBreakdownDTO getSleepBreakdown(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return dashboardService.getSleepBreakdown(userId);
    }
}
