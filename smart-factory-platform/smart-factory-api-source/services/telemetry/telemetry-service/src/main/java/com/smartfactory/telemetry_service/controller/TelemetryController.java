package com.smartfactory.telemetry_service.controller;

import com.smartfactory.telemetry_service.dto.CreateTelemetryRequest;
import com.smartfactory.telemetry_service.dto.TelemetryResponse;
import com.smartfactory.telemetry_service.entity.Telemetry;
import com.smartfactory.telemetry_service.service.TelemetryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/telemetry")
public class TelemetryController {

    public final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService){
        this.telemetryService =telemetryService;
    }

    @PostMapping
    public ResponseEntity<TelemetryResponse> create(@Valid @RequestBody
                                                        CreateTelemetryRequest createTelemetryRequest){
       TelemetryResponse response =telemetryService.create(createTelemetryRequest);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TelemetryResponse> getTelemetry(@PathVariable Long id){
      return ResponseEntity.ok(telemetryService.getTelemetry(id));
    }
}
