package com.scottlee.maintenance_tracker.config;

import com.scottlee.maintenance_tracker.entity.MaintenanceRule;
import com.scottlee.maintenance_tracker.repository.MaintenanceLogRepository;
import com.scottlee.maintenance_tracker.repository.MaintenanceRuleRepository;
import com.scottlee.maintenance_tracker.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StandardRuleSeeder implements CommandLineRunner {

    private final MaintenanceRuleRepository ruleRepository;

    @Override
    public void run(String... args) throws Exception {

        log.info("Syncing Scott's standard automotive maintenance rules...");

        List<MaintenanceRule> standardRules = List.of(
                MaintenanceRule.builder().serviceName("Oil Change").intervalMiles(8000).intervalMonths(12).build(),
                MaintenanceRule.builder().serviceName("Tire Rotation").intervalMiles(8000).intervalMonths(12).build(),
                MaintenanceRule.builder().serviceName("Brake Pads Replacement").intervalMiles(50000).intervalMonths(48).build(),
                MaintenanceRule.builder().serviceName("Battery Replacement").intervalMiles(40000).intervalMonths(48).build(),
                MaintenanceRule.builder().serviceName("Spark Plugs Replacement").intervalMiles(80000).intervalMonths(84).build()
        );

        // Professional Upsert Pattern: Updates standard rules without deleting user's custom rules!
        for (MaintenanceRule rule : standardRules) {
            MaintenanceRule existingRule = ruleRepository.findByServiceName(rule.getServiceName()).orElse(null);
            if (existingRule != null) {
                // Update existing standard rule if intervals have changed
                if (!existingRule.getIntervalMiles().equals(rule.getIntervalMiles()) ||
                        !existingRule.getIntervalMonths().equals(rule.getIntervalMonths())) {

                    MaintenanceRule updatedRule = MaintenanceRule.builder()
                            .id(existingRule.getId()) // Preserve existing Primary Key
                            .serviceName(rule.getServiceName())
                            .intervalMiles(rule.getIntervalMiles())
                            .intervalMonths(rule.getIntervalMonths())
                            .build();
                    ruleRepository.save(updatedRule);
                    log.info("Updated standard rule to latest specs: {}", rule.getServiceName());
                }
            } else {
                // Insert new standard rule if it does not exist
                ruleRepository.save(rule);
                log.info("Inserted new standard rule: {}", rule.getServiceName());
            }
        }

        log.info("Standard maintenance rules sync completed successfully!");
    }
}