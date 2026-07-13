package com.scottlee.maintenance_tracker.controller;

import com.scottlee.maintenance_tracker.dto.MaintenanceStatusDto;
import com.scottlee.maintenance_tracker.entity.MaintenanceLog;
import com.scottlee.maintenance_tracker.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;


    @GetMapping("/{vehicleId}/maintenance-status")
    public ResponseEntity<List<MaintenanceStatusDto>> getMaintenanceStatus(@PathVariable Long vehicleId) {
        List<MaintenanceStatusDto> report = vehicleService.getMaintenanceStatusReport(vehicleId);
        return ResponseEntity.ok(report);
    }
}
