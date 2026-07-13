package com.scottlee.maintenance_tracker.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceStatusDto {
    private String partName;
    private Integer intervalMiles;
    private Integer lastServiceMileage;
    private Integer milesSinceLastService;
    private Integer milesRemaining;
    private boolean isDue;
}