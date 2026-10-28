package com.smartfactory.telemetry_service.service;

import com.smartfactory.telemetry_service.entity.Telemetry;
import com.smartfactory.telemetry_service.event.TelemetryCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class TelemetryEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String topic;

    public TelemetryEventPublisher(KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${app.kafka.telemetry-topic:factory.telemetry.v1}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(Telemetry telemetry) {
        var event = new TelemetryCreatedEvent(UUID.randomUUID().toString(), telemetry.getMachineCode(),
                telemetry.getTimestamp(), String.valueOf(telemetry.getMachineState()), telemetry.getMetrics(), Instant.now());
        kafkaTemplate.send(topic, telemetry.getMachineCode(), event);
    }
}
