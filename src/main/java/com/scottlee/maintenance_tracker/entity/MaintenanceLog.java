package com.scottlee.maintenance_tracker.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "maintenance_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id",nullable = false)

    private Vehicle vehicle;

    @Column(nullable = false)
    private String partName;

    @Column(nullable = false)
    private LocalDate serviceDate;

    @Column(nullable = false)
    private Integer mileageAtService;

    private String notes;

}
