package com.scottlee.maintenance_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maintenance_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String partName;

    @Column(nullable = false)
    private Integer intervalMiles;

    @Column(nullable = false)
    private Integer intervalMonths;

}
