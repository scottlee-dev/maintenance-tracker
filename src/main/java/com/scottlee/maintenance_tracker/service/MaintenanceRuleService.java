package com.scottlee.maintenance_tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scottlee.maintenance_tracker.entity.MaintenanceRule;
import com.scottlee.maintenance_tracker.repository.MaintenanceRuleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaintenanceRuleService {

	private final MaintenanceRuleRepository ruleRepository;

	@Transactional(readOnly = true)
	public List<MaintenanceRule> getAllRules() {
		return ruleRepository.findAll();
	}

	@Transactional
	public MaintenanceRule addRule(MaintenanceRule rule) {
		return ruleRepository.save(rule);
	}

	@Transactional
	public MaintenanceRule updateRule(Long id, MaintenanceRule updatedRule) {
		MaintenanceRule existing = ruleRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Rule not found with ID: " + id));
		existing.setServiceName(updatedRule.getServiceName());
		existing.setIntervalMiles(updatedRule.getIntervalMiles());
		existing.setIntervalMonths(updatedRule.getIntervalMonths());
		existing.setFuelType(updatedRule.getFuelType());
		return ruleRepository.save(existing);
	}

	@Transactional
	public void deleteRule(Long id) {
		if (!ruleRepository.existsById(id)) {
			throw new IllegalArgumentException("Rule not found with ID: " + id);
		}
		ruleRepository.deleteById(id);
	}
}