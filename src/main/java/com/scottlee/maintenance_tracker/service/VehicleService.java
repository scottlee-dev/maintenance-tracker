package com.scottlee.maintenance_tracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scottlee.maintenance_tracker.dto.MaintenanceStatusDto;
import com.scottlee.maintenance_tracker.entity.MaintenanceLog;
import com.scottlee.maintenance_tracker.entity.MaintenanceRule;
import com.scottlee.maintenance_tracker.entity.Vehicle;
import com.scottlee.maintenance_tracker.repository.MaintenanceLogRepository;
import com.scottlee.maintenance_tracker.repository.MaintenanceRuleRepository;
import com.scottlee.maintenance_tracker.repository.VehicleRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.scottlee.maintenance_tracker.exception.VehicleNotFoundException;
@Service
@RequiredArgsConstructor
public class VehicleService {

	private final VehicleRepository vehicleRepository;
	private final MaintenanceRuleRepository ruleRepository;
	private final MaintenanceLogRepository logRepository;

	@Transactional(readOnly = true)
	public List<MaintenanceStatusDto> getMaintenanceStatusReport(Long vehicleId) {
		// 1. Find the vehicle
		Vehicle vehicle = vehicleRepository.findById(vehicleId)
				.orElseThrow(() -> new com.scottlee.maintenance_tracker.exception.VehicleNotFoundException("Vehicle not found with ID: " + vehicleId));

		List<MaintenanceRule> rules = ruleRepository.findAll();
		List<MaintenanceLog> logs = logRepository.findByVehicleIdOrderByServiceDateDesc(vehicleId);

		List<MaintenanceStatusDto> report = new ArrayList<>();

		for (MaintenanceRule rule : rules) {
			MaintenanceLog latestLog = logs.stream()
					.filter(log -> log.getPartName().equalsIgnoreCase(rule.getPartName())).findFirst().orElse(null);

			int lastServiceMileage = (latestLog != null) ? latestLog.getMileageAtService() : 0;
			int milesSinceLastService = vehicle.getCurrentMileage() - lastServiceMileage;
			int milesRemaining = rule.getIntervalMiles() - milesSinceLastService;
			boolean isDue = milesRemaining <= 0;
			report.add(
					MaintenanceStatusDto.builder().partName(rule.getPartName()).intervalMiles(rule.getIntervalMiles())
							.lastServiceMileage(lastServiceMileage).milesSinceLastService(milesSinceLastService)
							.milesRemaining(Math.max(0, milesRemaining)).isDue(isDue).build());
		}

		return report;

	}

}
