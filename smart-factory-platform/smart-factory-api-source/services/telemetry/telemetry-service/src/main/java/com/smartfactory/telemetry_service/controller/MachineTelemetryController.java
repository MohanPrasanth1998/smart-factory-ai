package com.smartfactory.telemetry_service.controller;

import com.smartfactory.telemetry_service.dto.TelemetryPageResponse;
import com.smartfactory.telemetry_service.dto.TelemetryResponse;
import com.smartfactory.telemetry_service.service.TelemetryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/machines")
public class MachineTelemetryController {

    private final TelemetryService telemetryService;

    public MachineTelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }


    @GetMapping("/{machineCode}/telemetry")
    public ResponseEntity<TelemetryPageResponse> getMachineTelemetry(@PathVariable String machineCode
                                                                    , @RequestParam(required = false) String cursor
                                                                    ,@RequestParam(defaultValue = "20") int limit){
        return  ResponseEntity.ok(telemetryService.getMachineTelemetry(machineCode,cursor,limit));
    }
}