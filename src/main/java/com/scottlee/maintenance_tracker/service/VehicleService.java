package com.scottlee.maintenance_tracker.service;

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

	// Updates existing vehicle details in PostgreSQL
	@Transactional
	public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
		Vehicle existing = vehicleRepository.findById(id)
				.orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + id));
		existing.setMake(updatedVehicle.getMake());
		existing.setModel(updatedVehicle.getModel());
		existing.setYear(updatedVehicle.getYear());
		existing.setCurrentMileage(updatedVehicle.getCurrentMileage());
		// NEW: Safely update fuelType/drivetrain when editing vehicle details
		existing.setFuelType(updatedVehicle.getFuelType());
		return vehicleRepository.save(existing);
	}

	// Deletes a vehicle from the garage (and safely clears its associated logs first)
	@Transactional
	public void deleteVehicle(Long id) {
		if (!vehicleRepository.existsById(id)) {
			throw new VehicleNotFoundException("Vehicle not found with ID: " + id);
		}
		// Optimized: Bulk delete logs to prevent N+1 query execution overhead
		List<MaintenanceLog> logs = logRepository.findByVehicleIdOrderByServiceDateDesc(id);
		logRepository.deleteAll(logs);
		vehicleRepository.deleteById(id);
	}

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

	// Generates a dynamic maintenance status report based on live database records and drivetrain rules
	@Transactional(readOnly = true)
	public List<MaintenanceStatusDto> getMaintenanceStatusReport(Long vehicleId) {
		// 1. Find the vehicle or throw custom exception if not found
		Vehicle vehicle = vehicleRepository.findById(vehicleId)
				.orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + vehicleId));

		// 2. Fetch all global rules and specific vehicle logs
		List<MaintenanceRule> allRules = ruleRepository.findAll();
		List<MaintenanceLog> logs = logRepository.findByVehicleIdOrderByServiceDateDesc(vehicleId);

		// Default to GASOLINE if fuelType is null (maintains backwards compatibility with Day 1 data)
		String vFuel = vehicle.getFuelType() == null ? "GASOLINE" : vehicle.getFuelType().toUpperCase();

		// 3. Smart Filtering: Only process maintenance rules applicable to this vehicle's specific drivetrain
		List<MaintenanceRule> applicableRules = allRules.stream()
				.filter(rule -> isRuleApplicable(rule, vFuel))
				.toList();

		// 4. Map rules to DTOs, accurately calculating negative overdue values
		return applicableRules.stream().map(rule -> {
			// Robustly find the highest historical mileage recorded for this service item
			int lastServiceMileage = logs.stream()
					.filter(log -> log.getServiceName().equalsIgnoreCase(rule.getServiceName()))
					.mapToInt(MaintenanceLog::getMileageAtService)
					.max()
					.orElse(0);

			int milesSinceLastService = vehicle.getCurrentMileage() - lastServiceMileage;
			// Retain negative numbers to allow the frontend to display exact overdue mileage!
			int milesRemaining = rule.getIntervalMiles() - milesSinceLastService;
			boolean isDue = milesRemaining <= 0;

			return MaintenanceStatusDto.builder()
					.serviceName(rule.getServiceName())
					.intervalMiles(rule.getIntervalMiles())
					.lastServiceMileage(lastServiceMileage)
					.milesSinceLastService(milesSinceLastService)
					.milesRemaining(milesRemaining)
					.isDue(isDue)
					.build();
		}).toList();
	}

	// Helper logic: determines if a specific maintenance rule applies to the vehicle's engine/drivetrain
	private boolean isRuleApplicable(MaintenanceRule rule, String vehicleFuelType) {
		String ruleFuel = rule.getFuelType() == null ? "ALL" : rule.getFuelType().toUpperCase();
		if ("ALL".equals(ruleFuel)) return true;
		if (ruleFuel.equals(vehicleFuelType)) return true;
		// Hybrid vehicles possess combustion engines and therefore require standard gasoline maintenance items
		if ("HYBRID".equals(vehicleFuelType) && "GASOLINE".equals(ruleFuel)) return true;
		return false;
	}
}