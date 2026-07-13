package com.scottlee.maintenance_tracker.repository;
import com.scottlee.maintenance_tracker.entity.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {
    List<MaintenanceLog> findByVehicleIdOrderByServiceDateDesc(Long vehicleId);
}
