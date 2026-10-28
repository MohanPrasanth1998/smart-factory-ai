package com.smartfactory.telemetry_service.client;

import com.smartfactory.telemetry_service.dto.MachineResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="machine-service",url = "${clients.machine.url}")
public interface MachineClient {

    @GetMapping("/api/v1/machines/{machineCode}")
    MachineResponse getMachineByCode(@PathVariable String machineCode);
}
