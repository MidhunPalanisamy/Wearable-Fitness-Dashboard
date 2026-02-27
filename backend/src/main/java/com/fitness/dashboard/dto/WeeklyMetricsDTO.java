package com.fitness.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeeklyMetricsDTO {
    private String date;
    private int steps;
    private int calories;
}
