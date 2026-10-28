package com.smartfactory.production.dto;
import jakarta.validation.constraints.Min;
public record UpdateProductionProgressRequest(@Min(0) int completedQuantity){}
