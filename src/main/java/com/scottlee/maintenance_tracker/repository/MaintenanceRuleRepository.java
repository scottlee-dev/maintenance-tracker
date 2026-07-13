package com.scottlee.maintenance_tracker.repository;

import com.scottlee.maintenance_tracker.entity.MaintenanceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MaintenanceRuleRepository extends JpaRepository<MaintenanceRule, Long> {
    Optional<MaintenanceRule> findByPartName(String partName);
}