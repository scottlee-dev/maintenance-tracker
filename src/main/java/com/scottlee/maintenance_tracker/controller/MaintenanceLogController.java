package com.scottlee.maintenance_tracker.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.scottlee.maintenance_tracker.entity.MaintenanceLog;
import com.scottlee.maintenance_tracker.service.MaintenanceLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vehicles/{vehicleId}/logs")
@RequiredArgsConstructor
public class MaintenanceLogController {

	private final MaintenanceLogService logService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)

	public MaintenanceLog addLog(@PathVariable Long vehicleId, @RequestBody MaintenanceLog log) {

		return logService.addLog(vehicleId, log);
	}

	@GetMapping
	public List<MaintenanceLog> getLogs(@PathVariable Long vehicleId) {

		return logService.getLogsByVehicleId(vehicleId);
	}
	@PutMapping("/{logId}")
	public MaintenanceLog updateLog(@PathVariable Long vehicleId, @PathVariable Long logId, @RequestBody MaintenanceLog log) {
		return logService.updateLog(vehicleId, logId, log);
	}

	@DeleteMapping("/{logId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLog(@PathVariable Long vehicleId, @PathVariable Long logId) {
		logService.deleteLog(vehicleId, logId);
	}

}
