package com.smartfactory.maintenance.entity;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="maintenance_records", indexes=@Index(name="idx_maintenance_machine_status",columnList="machine_code,status"))
public class MaintenanceRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="machine_code",nullable=false,length=64) private String machineCode;
 @Column(nullable=false,length=500) private String description;
 @Column(nullable=false,length=24) private String status;
 @Column(name="scheduled_at") private Instant scheduledAt;
 @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
 @PrePersist void init(){if(createdAt==null)createdAt=Instant.now();if(status==null)status="OPEN";}
 public Long getId(){return id;} public String getMachineCode(){return machineCode;} public void setMachineCode(String v){machineCode=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public Instant getScheduledAt(){return scheduledAt;} public void setScheduledAt(Instant v){scheduledAt=v;} public Instant getCreatedAt(){return createdAt;}
}
