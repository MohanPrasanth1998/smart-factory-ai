package com.smartfactory.factory.dto;

import com.smartfactory.factory.entity.FactoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateFactoryRequest(

        @NotBlank(message = "Factory name is required")
        @Size(max = 100, message = "Factory name must not exceed 100 characters")
        String name,

        @Size(max = 255, message = "Location must not exceed 255 characters")
        String location,

        @NotNull(message = "Factory status is required")
        FactoryStatus status
) {
}
