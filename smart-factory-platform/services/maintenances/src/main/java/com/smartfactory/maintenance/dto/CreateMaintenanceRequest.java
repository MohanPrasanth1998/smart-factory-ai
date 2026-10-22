package com.smartfactory.maintenance.dto;
import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Size; import java.time.Instant;
public record CreateMaintenanceRequest(@NotBlank @Size(max=64) String machineCode,@NotBlank @Size(max=500) String description,Instant scheduledAt){}
