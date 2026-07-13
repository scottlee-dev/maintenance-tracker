package com.scottlee.maintenance_tracker.config;

import com.scottlee.maintenance_tracker.entity.MaintenanceLog;
import com.scottlee.maintenance_tracker.entity.MaintenanceRule;
import com.scottlee.maintenance_tracker.entity.Vehicle;
import com.scottlee.maintenance_tracker.repository.MaintenanceLogRepository;
import com.scottlee.maintenance_tracker.repository.MaintenanceRuleRepository;
import com.scottlee.maintenance_tracker.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;
    private final MaintenanceRuleRepository ruleRepository;
    private final MaintenanceLogRepository logRepository;

    @Override
    public void run(String... args) throws Exception {
        if (vehicleRepository.count() == 0) {
            log.info("Starting Data Seeding for realistic local testing...");

            // 1. Create a Vehicle (2014 Honda CR-V with 150,000 miles)
            Vehicle myCar = Vehicle.builder()
                    .make("Honda")
                    .model("CR-V")
                    .year(2014)
                    .currentMileage(150000)
                    .build();
            vehicleRepository.save(myCar);

            // 2. Create standard Maintenance Rules
            MaintenanceRule oilRule = MaintenanceRule.builder()
                    .partName("0W-20 Synthetic Oil")
                    .intervalMiles(8000)
                    .intervalMonths(12)
                    .build();

            MaintenanceRule filterRule = MaintenanceRule.builder()
                    .partName("Engine Air Filter")
                    .intervalMiles(16000)
                    .intervalMonths(24)
                    .build();

            ruleRepository.saveAll(List.of(oilRule, filterRule));

            // 3. Create historical Maintenance Logs

            MaintenanceLog oilLog = MaintenanceLog.builder()
                    .vehicle(myCar)
                    .partName("0W-20 Synthetic Oil")
                    .serviceDate(LocalDate.now().minusMonths(4))
                    .mileageAtService(145000)
                    .notes("Regular synthetic oil change at local shop")
                    .build();

            MaintenanceLog filterLog = MaintenanceLog.builder()
                    .vehicle(myCar)
                    .partName("Engine Air Filter")
                    .serviceDate(LocalDate.now().minusMonths(18))
                    .mileageAtService(130000)
                    .notes("Replaced air filter during routine check")
                    .build();

            logRepository.saveAll(List.of(oilLog, filterLog));

            log.info("Data Seeding successfully completed! Test Vehicle ID is: {}", myCar.getId());
        }
    }
}