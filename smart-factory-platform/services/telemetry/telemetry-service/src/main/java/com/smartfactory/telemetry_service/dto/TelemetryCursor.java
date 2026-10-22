package com.smartfactory.telemetry_service.dto;

import java.time.Instant;

public record TelemetryCursor(
        Instant timestamp,
        Long id
) {
}
