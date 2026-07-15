package com.scottlee.maintenance_tracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    @Column(name = "model_year")
    private Integer year;
    @Column(name = "current_mileage")
    private Integer currentMileage;


}
