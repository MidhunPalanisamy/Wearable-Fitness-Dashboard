package com.fitness.dashboard.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Data
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String type;
    private String message;
    private String severity;
    private LocalDateTime timestamp;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
