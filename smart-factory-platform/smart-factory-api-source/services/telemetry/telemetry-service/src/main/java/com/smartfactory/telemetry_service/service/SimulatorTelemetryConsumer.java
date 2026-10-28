package com.smartfactory.telemetry_service.service;

import tools.jackson.databind.ObjectMapper;
import com.smartfactory.telemetry_service.entity.MachineState;
import com.smartfactory.telemetry_service.entity.Telemetry;
import com.smartfactory.telemetry_service.repository.TelemetryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Component
public class SimulatorTelemetryConsumer {
    private static final Logger log = LoggerFactory.getLogger(SimulatorTelemetryConsumer.class);
    private final ObjectMapper objectMapper;
    private final TelemetryRepository repository;
    private final MachineCacheService machineCacheService;
    private final TelemetryEventPublisher eventPublisher;

    public SimulatorTelemetryConsumer(ObjectMapper objectMapper, TelemetryRepository repository, MachineCacheService machineCacheService, TelemetryEventPublisher eventPublisher) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.machineCacheService = machineCacheService;
        this.eventPublisher = eventPublisher;
    }

    @KafkaListener(topics = "${app.kafka.ingest-topic:factory.telemetry.ingest.v1}", groupId = "telemetry-service")
    public void consume(String payload) {
        try {
            Map<String, Object> event = objectMapper.readValue(payload, Map.class);
            String machineCode = String.valueOf(event.get("machineCode"));
            machineCacheService.getMachine(machineCode); // Reject telemetry for unknown machines.
            Instant timestamp = Instant.parse(String.valueOf(event.get("timestamp")));
            String rawState = String.valueOf(event.getOrDefault("machineState", "RUNNING"));
            MachineState state = switch (rawState.toUpperCase()) {
                case "FAULT", "WARNING" -> MachineState.FAULT;
                case "IDLE" -> MachineState.IDLE;
                case "STOPPED" -> MachineState.STOPPED;
                default -> MachineState.RUNNING;
            };
            Map<String, Object> rawMetrics = (Map<String, Object>) event.get("metrics");
            Map<String, BigDecimal> metrics = rawMetrics.entrySet().stream().collect(java.util.stream.Collectors.toMap(
                    Map.Entry::getKey, e -> new BigDecimal(String.valueOf(e.getValue()))));
            Telemetry telemetry = new Telemetry();
            telemetry.setMachineCode(machineCode);
            telemetry.setTimestamp(timestamp);
            telemetry.setMachineState(state);
            telemetry.setMetrics(metrics);
            telemetry.setCreatedAt(Instant.now());
            Telemetry saved = repository.save(telemetry);
            eventPublisher.publish(saved);
            log.debug("Persisted simulator telemetry for machine {}", machineCode);
        } catch (Exception ex) {
            log.error("Unable to consume telemetry event; payload will be retried by Kafka error handler: {}", ex.getMessage());
            throw new IllegalArgumentException("Invalid telemetry event", ex);
        }
    }
}
