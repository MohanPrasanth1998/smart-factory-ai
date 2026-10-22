package com.smartfactory.machine.entity;

import com.smartfactory.machine.util.MachineStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name ="machines",uniqueConstraints = {@UniqueConstraint(
        name = "uk_machine_code",
        columnNames = "machine_code"
)})
@Getter
@Setter
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "machine_code",nullable = false,length = 20)
    private String machineCode;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String type;
    @Column(name = "factory_code",nullable = false,length = 20)
    private String factoryCode;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MachineStatus status;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
