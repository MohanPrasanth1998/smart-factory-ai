package com.smartfactory.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(

        @NotBlank(message = "Message must not be Blank")
        @Size(
                min = 1,
                max = 50000,
                message = "Message must be between 1 and 4000 characters"
        )
        String message
) {
}
