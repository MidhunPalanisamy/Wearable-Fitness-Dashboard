package com.fitness.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SummaryDTO {
    private int steps;
    private int calories;
    private int avgHeartRate;
    private double sleepHours;
}
