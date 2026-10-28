package com.smartfactory.telemetry_service.event;

import java.time.Instant;
import java.util.Map;
import java.math.BigDecimal;

public record TelemetryCreatedEvent(String eventId, String machineCode, Instant timestamp, String machineState, Map<String, BigDecimal> metrics, Instant receivedAt) {}
