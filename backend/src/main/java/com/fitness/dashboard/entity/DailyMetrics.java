package com.fitness.dashboard.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "daily_metrics")
@Data
public class DailyMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate date;
    private int steps;
    private int calories;
    private double sleepHours;
    private double deepSleep;
    private double lightSleep;
    private double remSleep;
    private int avgHeartRate;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
