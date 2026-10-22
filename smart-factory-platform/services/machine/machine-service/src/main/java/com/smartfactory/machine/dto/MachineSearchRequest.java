package com.smartfactory.machine.dto;

import com.smartfactory.machine.util.MachineStatus;

public record MachineSearchRequest(
        MachineStatus status,
        String factoryCode,
        String type,
        String search
) {
}
