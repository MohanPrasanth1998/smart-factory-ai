package com.smartfactory.simulator;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/v1/simulator")
public class SimulatorController {
    private final DeviceSimulatorService simulator;
    private final AtomicBoolean running = new AtomicBoolean(false);
    public SimulatorController(DeviceSimulatorService simulator) { this.simulator = simulator; }
    @PostMapping("/devices/{machineCode}/telemetry")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TelemetryEvent emit(@PathVariable @NotBlank String machineCode) { return simulator.emit(machineCode); }
    @PostMapping("/run")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, Object> run(@Valid @RequestBody SimulationRequest request) {
        if (!running.compareAndSet(false, true)) return Map.of("status", "already_running");
        CompletableFuture.runAsync(() -> { try { for (int i=0; i<request.count() && running.get(); i++) { simulator.emit(request.machineCode()); Thread.sleep(request.intervalMs()); } } catch (InterruptedException e) { Thread.currentThread().interrupt(); } finally { running.set(false); } });
        return Map.of("status", "started", "machineCode", request.machineCode(), "count", request.count(), "intervalMs", request.intervalMs());
    }
    @PostMapping("/stop") public Map<String, String> stop() { running.set(false); return Map.of("status", "stop_requested"); }
    @GetMapping("/health") public Map<String, Object> health() { return Map.of("status", "UP", "simulationRunning", running.get()); }
}
