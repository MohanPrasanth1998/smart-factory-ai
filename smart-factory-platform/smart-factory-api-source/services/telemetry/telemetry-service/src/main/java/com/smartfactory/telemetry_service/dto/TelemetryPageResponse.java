package com.smartfactory.telemetry_service.dto;

import java.util.List;

public record TelemetryPageResponse(
        List<TelemetryResponse> content,
        String nextCursor,
        boolean hasNext
) {
}
