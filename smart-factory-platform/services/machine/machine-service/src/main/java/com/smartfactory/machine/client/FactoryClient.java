package com.smartfactory.machine.client;

import com.smartfactory.machine.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "factory-service",
             url ="${clients.factory.url}",
        configuration = FeignConfig.class)
public interface FactoryClient {
    @GetMapping("/api/v1/factories/{factoryCode}")
    FactoryResponse getFactoryByCode(@PathVariable String factoryCode);
}
