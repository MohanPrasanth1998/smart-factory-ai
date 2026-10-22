package com.smartfactory.maintenance.dto;
import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Pattern;
public record UpdateMaintenanceStatusRequest(@NotBlank @Pattern(regexp="OPEN|SCHEDULED|IN_PROGRESS|COMPLETED|CANCELLED") String status){}
