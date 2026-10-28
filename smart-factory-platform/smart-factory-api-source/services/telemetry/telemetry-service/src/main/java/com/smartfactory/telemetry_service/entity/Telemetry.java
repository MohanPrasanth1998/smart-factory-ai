package com.smartfactory.telemetry_service.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "telemetry")
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "machine_code", nullable = false, length = 20)
    private String machineCode;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "machine_state", nullable = false, length = 20)
    private MachineState machineState;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metrics", nullable = false, columnDefinition = "jsonb")
    private Map<String, BigDecimal> metrics;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Telemetry() {
    }
}