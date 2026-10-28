package com.smartfactory.machine.dto;


import com.smartfactory.machine.util.MachineStatus;

import java.time.LocalDateTime;

public record MachineResponse(
        Long id,
        String machineCode,
        String name,
        String type,
        String factoryCode,
        MachineStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}