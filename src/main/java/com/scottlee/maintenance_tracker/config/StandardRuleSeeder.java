package com.scottlee.maintenance_tracker.config;

import com.scottlee.maintenance_tracker.entity.MaintenanceRule;
import com.scottlee.maintenance_tracker.repository.MaintenanceRuleRepository;
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
        log.info("Syncing smart fuel-type maintenance rules into PostgreSQL...");

        List<MaintenanceRule> standardRules = List.of(
                MaintenanceRule.builder().serviceName("Oil Change").intervalMiles(8000).intervalMonths(12).fuelType("GASOLINE").build(),
                MaintenanceRule.builder().serviceName("Tire Rotation").intervalMiles(8000).intervalMonths(12).fuelType("ALL").build(),
                MaintenanceRule.builder().serviceName("Brake Pads Replacement").intervalMiles(50000).intervalMonths(48).fuelType("ALL").build(),
                MaintenanceRule.builder().serviceName("12V Battery Replacement").intervalMiles(40000).intervalMonths(48).fuelType("ALL").build(),
                MaintenanceRule.builder().serviceName("Spark Plugs Replacement").intervalMiles(80000).intervalMonths(84).fuelType("GASOLINE").build(),
                MaintenanceRule.builder().serviceName("EV Battery & Coolant Check").intervalMiles(20000).intervalMonths(24).fuelType("EV").build()
        );

        for (MaintenanceRule rule : standardRules) {
            MaintenanceRule existingRule = ruleRepository.findByServiceName(rule.getServiceName()).orElse(null);
            if (existingRule != null) {
                MaintenanceRule updatedRule = MaintenanceRule.builder()
                        .id(existingRule.getId())
                        .serviceName(rule.getServiceName())
                        .intervalMiles(rule.getIntervalMiles())
                        .intervalMonths(rule.getIntervalMonths())
                        .fuelType(rule.getFuelType())
                        .build();
                ruleRepository.save(updatedRule);
                log.info("Updated rule specs for: {} [{}]", rule.getServiceName(), rule.getFuelType());
            } else {
                ruleRepository.save(rule);
                log.info("Inserted new rule: {} [{}]", rule.getServiceName(), rule.getFuelType());
            }
        }

        log.info("Smart fuel-type maintenance rules sync completed successfully!");
    }
}