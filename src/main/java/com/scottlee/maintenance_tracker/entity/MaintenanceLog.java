package com.scottlee.maintenance_tracker.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "vehicle_id")
	private Long vehicleId;
	@Column(name = "part_name")
	private String serviceName;
	@Column(name = "service_date")
	private LocalDate serviceDate;
	@Column(name = "mileage_at_service")
	private Integer mileageAtService;

	private BigDecimal cost;

	private String notes;
}