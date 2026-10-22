package com.smartfactory.simulator;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SimulationRequest(@NotBlank String machineCode, @Min(1) @Max(1000) int count, @Min(100) @Max(60000) long intervalMs) {}
