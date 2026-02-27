package com.fitness.dashboard.service;

import com.fitness.dashboard.dto.*;
import com.fitness.dashboard.entity.*;
import com.fitness.dashboard.repository.*;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DailyMetricsRepository dailyMetricsRepository;
    
    @Autowired
    private HeartRateLogRepository heartRateLogRepository;
    
    @Autowired
    private AlertRepository alertRepository;
    
    private Random random = new Random();
    private Faker faker = new Faker();
    
    public void generateUserData(User user) {
        // Check if data already exists
        if (dailyMetricsRepository.findByUserAndDate(user, LocalDate.now()).isPresent()) {
            return;
        }
        
        // Generate 30 days of daily metrics
        for (int i = 0; i < 30; i++) {
            DailyMetrics metrics = new DailyMetrics();
            metrics.setUser(user);
            metrics.setDate(LocalDate.now().minusDays(i));
            metrics.setSteps(3000 + random.nextInt(12000));
            metrics.setCalories(1500 + random.nextInt(1500));
            metrics.setAvgHeartRate(60 + random.nextInt(40));
            
            double totalSleep = 5 + random.nextDouble() * 4;
            metrics.setSleepHours(Math.round(totalSleep * 10.0) / 10.0);
            metrics.setDeepSleep(Math.round(totalSleep * 0.3 * 10.0) / 10.0);
            metrics.setLightSleep(Math.round(totalSleep * 0.5 * 10.0) / 10.0);
            metrics.setRemSleep(Math.round(totalSleep * 0.2 * 10.0) / 10.0);
            
            dailyMetricsRepository.save(metrics);
            
            // Generate alerts based on metrics
            if (metrics.getAvgHeartRate() > 95) {
                Alert alert = new Alert();
                alert.setUser(user);
                alert.setType("High Heart Rate");
                alert.setMessage("Average heart rate exceeded 95 bpm");
                alert.setSeverity("HIGH");
                alert.setTimestamp(metrics.getDate().atTime(12, 0));
                alertRepository.save(alert);
            }
            
            if (metrics.getSleepHours() < 6) {
                Alert alert = new Alert();
                alert.setUser(user);
                alert.setType("Low Sleep");
                alert.setMessage("Sleep duration below 6 hours");
                alert.setSeverity("MEDIUM");
                alert.setTimestamp(metrics.getDate().atTime(8, 0));
                alertRepository.save(alert);
            }
            
            if (metrics.getSteps() < 4000) {
                Alert alert = new Alert();
                alert.setUser(user);
                alert.setType("Low Activity");
                alert.setMessage("Daily steps below 4000");
                alert.setSeverity("LOW");
                alert.setTimestamp(metrics.getDate().atTime(20, 0));
                alertRepository.save(alert);
            }
        }
        
        // Generate 24 hours of heart rate logs (every 10 minutes)
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 144; i++) {
            HeartRateLog log = new HeartRateLog();
            log.setUser(user);
            log.setTimestamp(now.minusMinutes(i * 10));
            
            // Random spikes for workout simulation
            if (random.nextDouble() < 0.1) {
                log.setHeartRate(120 + random.nextInt(40));
            } else {
                log.setHeartRate(60 + random.nextInt(40));
            }
            
            heartRateLogRepository.save(log);
        }
    }
    
    public SummaryDTO getTodaySummary(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        DailyMetrics today = dailyMetricsRepository
            .findByUserAndDate(user, LocalDate.now())
            .orElse(new DailyMetrics());
        
        return new SummaryDTO(
            today.getSteps(),
            today.getCalories(),
            today.getAvgHeartRate(),
            today.getSleepHours()
        );
    }
    
    public List<WeeklyMetricsDTO> getWeeklyMetrics(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(6);
        
        List<DailyMetrics> metrics = dailyMetricsRepository
            .findByUserAndDateBetweenOrderByDateDesc(user, startDate, endDate);
        
        return metrics.stream()
            .map(m -> new WeeklyMetricsDTO(
                m.getDate().format(DateTimeFormatter.ofPattern("MM/dd")),
                m.getSteps(),
                m.getCalories()
            ))
            .collect(Collectors.toList());
    }
    
    public List<HeartRateDTO> getHeartRateLogs(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusHours(24);
        
        List<HeartRateLog> logs = heartRateLogRepository
            .findByUserAndTimestampBetweenOrderByTimestampAsc(user, start, end);
        
        return logs.stream()
            .map(log -> new HeartRateDTO(
                log.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")),
                log.getHeartRate()
            ))
            .collect(Collectors.toList());
    }
    
    public List<AlertDTO> getAlerts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Alert> alerts = alertRepository.findByUserOrderByTimestampDesc(user);
        
        return alerts.stream()
            .limit(10)
            .map(alert -> new AlertDTO(
                alert.getType(),
                alert.getMessage(),
                alert.getSeverity(),
                alert.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            ))
            .collect(Collectors.toList());
    }
    
    public SleepBreakdownDTO getSleepBreakdown(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        DailyMetrics today = dailyMetricsRepository
            .findByUserAndDate(user, LocalDate.now())
            .orElse(new DailyMetrics());
        
        return new SleepBreakdownDTO(
            today.getDeepSleep(),
            today.getLightSleep(),
            today.getRemSleep()
        );
    }
}
