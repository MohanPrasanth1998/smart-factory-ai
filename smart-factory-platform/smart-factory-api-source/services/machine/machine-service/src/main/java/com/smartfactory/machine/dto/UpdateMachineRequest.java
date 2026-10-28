package com.smartfactory.machine.dto;

import com.smartfactory.machine.util.MachineStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateMachineRequest(

        @NotBlank(message = "Machine name is required")
        @Size(max = 100, message = "Machine name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Machine type is required")
        @Size(max = 100, message = "Machine type must not exceed 100 characters")
        String type,

        @NotBlank(message = "Factory code is required")
        @Size(max = 20, message = "Factory code must not exceed 20 characters")
        String factoryCode,

        @NotNull(message = "Machine status is required")
        MachineStatus status
) {
}