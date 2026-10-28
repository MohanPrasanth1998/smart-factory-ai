package com.smartfactory.machine.controller;

import com.smartfactory.machine.client.FactoryClient;
import com.smartfactory.machine.client.FactoryResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class FeignTestController {

    private final FactoryClient factoryClient;

    public FeignTestController(FactoryClient factoryClient) {
        this.factoryClient = factoryClient;
    }

    @GetMapping("/factory/{factoryCode}")
    public FactoryResponse getFactory(
            @PathVariable String factoryCode) {

        return factoryClient.getFactoryByCode(factoryCode);
    }
}