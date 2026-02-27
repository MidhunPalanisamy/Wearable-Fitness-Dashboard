package com.fitness.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlertDTO {
    private String type;
    private String message;
    private String severity;
    private String timestamp;
}
