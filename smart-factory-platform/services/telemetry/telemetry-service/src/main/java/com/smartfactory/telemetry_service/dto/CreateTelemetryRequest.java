package com.smartfactory.telemetry_service.dto;


import com.smartfactory.telemetry_service.entity.MachineState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record CreateTelemetryRequest(

        @NotBlank(message = "Machine code is required")
        String machineCode,

        @NotNull(message = "Timestamp is required")
        Instant timestamp,

        @NotNull(message = "Machine state is required")
        MachineState machineState,

        @NotEmpty(message = "Metrics are required")
        Map<String, BigDecimal> metrics
) {
}