package com.smartfactory.notification;

import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Map;

@Component
public class TelemetryEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(TelemetryEventConsumer.class);
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redis;

    public TelemetryEventConsumer(ObjectMapper objectMapper, StringRedisTemplate redis) {
        this.objectMapper = objectMapper;
        this.redis = redis;
    }

    @KafkaListener(topics = "${app.kafka.telemetry-topic:factory.telemetry.v1}", groupId = "${spring.kafka.consumer.group-id:notification-service}")
    public void onTelemetry(String eventJson) {
        try {
            Map<String, Object> event = objectMapper.readValue(eventJson, Map.class);
            String machineCode = String.valueOf(event.getOrDefault("machineCode", "unknown"));
            redis.opsForValue().set("notification:telemetry:last:" + machineCode, eventJson, Duration.ofHours(1));
            redis.opsForValue().increment("notification:telemetry:events");
            if ("FAULT".equalsIgnoreCase(String.valueOf(event.get("machineState")))) {
                redis.opsForValue().set("notification:alert:last:" + machineCode, eventJson, Duration.ofDays(1));
                log.warn("Machine fault alert recorded for {}", machineCode);
            } else {
                log.info("Cached telemetry event for machine {}", machineCode);
            }
        } catch (Exception ex) {
            log.error("Failed to process telemetry event: {}", ex.getMessage());
            throw new IllegalArgumentException("Invalid telemetry event", ex);
        }
    }
}
