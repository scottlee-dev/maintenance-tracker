package com.scottlee.maintenance_tracker.service;

import com.scottlee.maintenance_tracker.dto.MaintenanceStatusDto;
import com.scottlee.maintenance_tracker.entity.MaintenanceLog;
import com.scottlee.maintenance_tracker.entity.MaintenanceRule;
import com.scottlee.maintenance_tracker.entity.Vehicle;
import com.scottlee.maintenance_tracker.exception.VehicleNotFoundException;
import com.scottlee.maintenance_tracker.repository.MaintenanceLogRepository;
import com.scottlee.maintenance_tracker.repository.MaintenanceRuleRepository;
import com.scottlee.maintenance_tracker.repository.VehicleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private MaintenanceRuleRepository ruleRepository;

    @Mock
    private MaintenanceLogRepository logRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    @DisplayName("Should correctly calculate remaining mileage and service due status based on vehicle odometer")
    void getMaintenanceStatusReport_Success() {
        // Given: Setup mock vehicle, maintenance rule, and historical log data
        Long vehicleId = 1L;
        Vehicle mockVehicle = Vehicle.builder()
                .id(vehicleId)
                .currentMileage(150000)
                .build();

        MaintenanceRule mockRule = MaintenanceRule.builder()
                .serviceName("0W-20 Synthetic Oil")
                .intervalMiles(8000)
                .build();

        MaintenanceLog mockLog = MaintenanceLog.builder()
                .serviceName("0W-20 Synthetic Oil")
                .mileageAtService(145000)
                .build();

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(mockVehicle));
        when(ruleRepository.findAll()).thenReturn(List.of(mockRule));
        when(logRepository.findByVehicleIdOrderByServiceDateDesc(vehicleId)).thenReturn(List.of(mockLog));

        // When: Execute the business logic to generate the maintenance status report
        List<MaintenanceStatusDto> result = vehicleService.getMaintenanceStatusReport(vehicleId);

        // Then: Verify remaining mileage calculation (8000 - (150000 - 145000) = 3000 miles remaining) and alert status
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getServiceName()).isEqualTo("0W-20 Synthetic Oil");
        assertThat(result.getFirst().getMilesRemaining()).isEqualTo(3000);
        assertThat(result.getFirst().isDue()).isFalse();
    }

    @Test
    @DisplayName("Should throw VehicleNotFoundException when querying a non-existent vehicle ID")
    void getMaintenanceStatusReport_VehicleNotFound() {
        // Given: Configure repository to return an empty Optional for an invalid vehicle ID
        Long invalidId = 9999L;
        when(vehicleRepository.findById(invalidId)).thenReturn(Optional.empty());

        // When & Then: Assert that querying an invalid ID throws the expected exception
        assertThatThrownBy(() -> vehicleService.getMaintenanceStatusReport(invalidId))
                .isInstanceOf(VehicleNotFoundException.class)
                .hasMessageContaining("Vehicle not found with ID");
    }
}