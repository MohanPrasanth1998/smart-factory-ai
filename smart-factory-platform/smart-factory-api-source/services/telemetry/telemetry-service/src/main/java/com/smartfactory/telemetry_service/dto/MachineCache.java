package com.smartfactory.telemetry_service.dto;

public record MachineCache(
        String machineCode,
        String name,
        String factoryCode,
        String status
) {
}
