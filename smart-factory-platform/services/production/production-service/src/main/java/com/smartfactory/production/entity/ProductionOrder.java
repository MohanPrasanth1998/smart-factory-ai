package com.smartfactory.production.entity;
import jakarta.persistence.*; import java.time.Instant;
@Entity @Table(name="production_orders",indexes=@Index(name="idx_production_machine_status",columnList="machine_code,status"))
public class ProductionOrder {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(name="machine_code",nullable=false,length=64) private String machineCode; @Column(name="product_code",nullable=false,length=64) private String productCode; @Column(name="planned_quantity",nullable=false) private int plannedQuantity; @Column(name="completed_quantity",nullable=false) private int completedQuantity; @Column(nullable=false,length=24) private String status; @Column(name="created_at",nullable=false,updatable=false) private Instant createdAt;
 @PrePersist void init(){if(createdAt==null)createdAt=Instant.now();if(status==null)status="PLANNED";}
 public Long getId(){return id;} public String getMachineCode(){return machineCode;} public void setMachineCode(String v){machineCode=v;} public String getProductCode(){return productCode;} public void setProductCode(String v){productCode=v;} public int getPlannedQuantity(){return plannedQuantity;} public void setPlannedQuantity(int v){plannedQuantity=v;} public int getCompletedQuantity(){return completedQuantity;} public void setCompletedQuantity(int v){completedQuantity=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;} public Instant getCreatedAt(){return createdAt;}
}
