package com.fitness.dashboard.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendOtpResponse {
    private String message;
    private String otp;
}
