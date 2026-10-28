package com.smartfactory.telemetry_service.dto;


public record MachineResponse(
        Long id,
        String machineCode,
        String name,
        String type,
        String factoryCode,
        String status
) {
}