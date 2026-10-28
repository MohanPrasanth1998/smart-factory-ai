package com.smartfactory.telemetry_service.dto;



import com.smartfactory.telemetry_service.entity.MachineState;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record TelemetryResponse(
        Long id,
        String machineCode,
        Instant timestamp,
        MachineState machineState,
        Map<String, BigDecimal> metrics,
        Instant createdAt
) {
}