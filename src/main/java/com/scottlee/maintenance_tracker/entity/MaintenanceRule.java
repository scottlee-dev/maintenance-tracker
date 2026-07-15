package com.scottlee.maintenance_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_name")
    private String serviceName;
    @Column(name = "interval_miles")
    private Integer intervalMiles;
    @Column(name = "interval_months")

    private Integer intervalMonths;
}