package com.scottlee.maintenance_tracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scottlee.maintenance_tracker.dto.MaintenanceStatusDto;
import com.scottlee.maintenance_tracker.entity.MaintenanceLog;
import com.scottlee.maintenance_tracker.entity.MaintenanceRule;
import com.scottlee.maintenance_tracker.entity.Vehicle;
import com.scottlee.maintenance_tracker.exception.VehicleNotFoundException;
import com.scottlee.maintenance_tracker.repository.MaintenanceLogRepository;
import com.scottlee.maintenance_tracker.repository.MaintenanceRuleRepository;
import com.scottlee.maintenance_tracker.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleService {

	private final VehicleRepository vehicleRepository;
	private final MaintenanceRuleRepository ruleRepository;
	private final MaintenanceLogRepository logRepository;

	// Registers a new vehicle dynamically into the database (For SaaS capability)
	@Transactional
	public Vehicle registerVehicle(Vehicle vehicle) {
		return vehicleRepository.save(vehicle);
	}

	// Retrieves all registered vehicles for user tracking
	@Transactional(readOnly = true)
	public List<Vehicle> getAllVehicles() {
		return vehicleRepository.findAll();
	}

	// Generates a dynamic maintenance status report based on live database records
	@Transactional(readOnly = true)
	public List<MaintenanceStatusDto> getMaintenanceStatusReport(Long vehicleId) {
		// 1. Find the vehicle or throw custom exception if not found
		Vehicle vehicle = vehicleRepository.findById(vehicleId)
				.orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + vehicleId));

		// 2. Fetch all global rules and specific vehicle logs
		List<MaintenanceRule> rules = ruleRepository.findAll();
		List<MaintenanceLog> logs = logRepository.findByVehicleIdOrderByServiceDateDesc(vehicleId);

		List<MaintenanceStatusDto> report = new ArrayList<>();

		// 3. Loop through rules and match with the latest logs using serviceName
		// instead of partName
		for (MaintenanceRule rule : rules) {
			MaintenanceLog latestLog = logs.stream()
					.filter(log -> log.getServiceName().equalsIgnoreCase(rule.getServiceName())).findFirst()
					.orElse(null);

			int lastServiceMileage = (latestLog != null) ? latestLog.getMileageAtService() : 0;
			int milesSinceLastService = vehicle.getCurrentMileage() - lastServiceMileage;
			int milesRemaining = rule.getIntervalMiles() - milesSinceLastService;
			boolean isDue = milesRemaining <= 0;

			report.add(MaintenanceStatusDto.builder().serviceName(rule.getServiceName()) // Updated from partName to
																							// serviceName
					.intervalMiles(rule.getIntervalMiles()).lastServiceMileage(lastServiceMileage)
					.milesSinceLastService(milesSinceLastService).milesRemaining(Math.max(0, milesRemaining))
					.isDue(isDue).build());
		}

		return report;
	}
}