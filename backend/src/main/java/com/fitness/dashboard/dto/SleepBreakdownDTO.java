package com.fitness.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SleepBreakdownDTO {
    private double deepSleep;
    private double lightSleep;
    private double remSleep;
}
