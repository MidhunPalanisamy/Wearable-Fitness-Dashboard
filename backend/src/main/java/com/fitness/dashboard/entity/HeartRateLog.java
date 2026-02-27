package com.fitness.dashboard.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "heart_rate_log")
@Data
public class HeartRateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime timestamp;
    private int heartRate;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
