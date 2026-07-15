package com.scottlee.maintenance_tracker.service;

import com.scottlee.maintenance_tracker.entity.MaintenanceLog;
import com.scottlee.maintenance_tracker.exception.VehicleNotFoundException;
import com.scottlee.maintenance_tracker.repository.MaintenanceLogRepository;
import com.scottlee.maintenance_tracker.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceLogService {
    private final MaintenanceLogRepository logRepository;
    private final VehicleRepository vehicleRepository;

    @Transactional
    public MaintenanceLog addLog(Long vehicleId, MaintenanceLog log) {
        if(!vehicleRepository.existsById(vehicleId)){
            throw new VehicleNotFoundException("Cannot add log. Vehicle not found with ID: " +vehicleId);
        }
        MaintenanceLog logToSave = MaintenanceLog.builder()
                .vehicleId(vehicleId)
                .serviceName(log.getServiceName())
                .serviceDate(log.getServiceDate())
                .mileageAtService(log.getMileageAtService())
                .cost(log.getCost())
                .notes(log.getNotes())
                .build();

        return logRepository.save(logToSave);
    }
    @Transactional(readOnly = true)
    public List<MaintenanceLog> getLogsByVehicleId(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new VehicleNotFoundException("Vehicle not found with ID: " + vehicleId);
        }
        return logRepository.findByVehicleIdOrderByServiceDateDesc(vehicleId);
    }
}
