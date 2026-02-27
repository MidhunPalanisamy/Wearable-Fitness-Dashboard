package com.fitness.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HeartRateDTO {
    private String time;
    private int heartRate;
}
