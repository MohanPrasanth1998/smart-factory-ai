package com.smartfactory.production.dto;
import jakarta.validation.constraints.Min; import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Size;
public record CreateProductionOrderRequest(@NotBlank @Size(max=64) String machineCode,@NotBlank @Size(max=64) String productCode,@Min(1) int plannedQuantity){}
