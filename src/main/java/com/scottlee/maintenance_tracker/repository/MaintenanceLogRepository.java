package com.scottlee.maintenance_tracker.repository;

import com.scottlee.maintenance_tracker.entity.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {
    // Retrieves all historical logs for a specific vehicle ordered by service date
    List<MaintenanceLog> findByVehicleIdOrderByServiceDateDesc(Long vehicleId);

    // Updated from findByPartName to findByServiceName if you have custom search methods
    List<MaintenanceLog> findByServiceName(String serviceName);
}