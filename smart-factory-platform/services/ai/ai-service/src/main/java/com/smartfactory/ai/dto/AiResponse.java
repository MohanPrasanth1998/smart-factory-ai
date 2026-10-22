package com.smartfactory.ai.dto;

public record AiResponse(

        String answer,
        String category,
        String confidence
) {
}
