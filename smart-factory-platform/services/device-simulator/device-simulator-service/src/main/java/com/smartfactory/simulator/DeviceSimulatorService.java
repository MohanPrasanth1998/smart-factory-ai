package com.smartfactory.simulator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DeviceSimulatorService {
    private final KafkaTemplate<String, Object> kafka;
    private final String topic;
    public DeviceSimulatorService(KafkaTemplate<String, Object> kafka, @Value("${app.kafka.ingest-topic:factory.telemetry.ingest.v1}") String topic) { this.kafka = kafka; this.topic = topic; }
    public TelemetryEvent emit(String machineCode) {
        var random = ThreadLocalRandom.current();
        double temperature = Math.round((55 + random.nextDouble(-5, 35)) * 100.0) / 100.0;
        double vibration = Math.round(random.nextDouble(0.1, 8.0) * 100.0) / 100.0;
        String state = temperature > 80 || vibration > 6.5 ? "FAULT" : "RUNNING";
        Instant now = Instant.now();
        var event = new TelemetryEvent(UUID.randomUUID().toString(), machineCode, now, state, Map.of("temperatureC", temperature, "vibrationMmS", vibration, "rpm", random.nextInt(900, 3000), "powerKw", Math.round(random.nextDouble(5, 30) * 100.0) / 100.0), now);
        kafka.send(topic, machineCode, event);
        return event;
    }
}
