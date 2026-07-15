package com.scottlee.maintenance_tracker.controller;

import com.scottlee.maintenance_tracker.entity.MaintenanceRule;
import com.scottlee.maintenance_tracker.service.MaintenanceRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class MaintenanceRuleController {

    private final MaintenanceRuleService ruleService;

    @GetMapping
    public List<MaintenanceRule> getRules() {
        return ruleService.getAllRules();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceRule addRule(@RequestBody MaintenanceRule rule) {
        return ruleService.addRule(rule);
    }

    @PutMapping("/{id}")
    public MaintenanceRule updateRule(@PathVariable Long id, @RequestBody MaintenanceRule rule) {
        return ruleService.updateRule(id, rule);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
    }
}