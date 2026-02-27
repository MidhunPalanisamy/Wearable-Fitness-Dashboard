package com.fitness.dashboard.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Data
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String mobileNumber;
    
    @Column(nullable = false)
    private String otpCode;
    
    @Column(nullable = false)
    private LocalDateTime expiryTime;
    
    @Column(nullable = false)
    private boolean verified = false;
}
