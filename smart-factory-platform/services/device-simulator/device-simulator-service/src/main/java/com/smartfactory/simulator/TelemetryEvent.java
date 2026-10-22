package com.smartfactory.simulator;

import java.time.Instant;
import java.util.Map;

public record TelemetryEvent(String eventId, String machineCode, Instant timestamp, String machineState, Map<String, Object> metrics, Instant receivedAt) {}
