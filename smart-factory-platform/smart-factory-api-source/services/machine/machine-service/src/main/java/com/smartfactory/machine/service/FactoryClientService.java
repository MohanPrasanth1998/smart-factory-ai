package com.smartfactory.machine.service;

import com.smartfactory.machine.client.FactoryClient;
import com.smartfactory.machine.client.FactoryResponse;
import com.smartfactory.machine.exception.FactoryNotFoundException;
import com.smartfactory.machine.exception.FactoryServiceUnavailableException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

@Service
public class FactoryClientService {

    private final FactoryClient factoryClient;

    public FactoryClientService(FactoryClient factoryClient) {
        this.factoryClient = factoryClient;
    }

    @CircuitBreaker(
            name = "factoryService",
            fallbackMethod = "factoryServiceFallback"
    )
    public FactoryResponse getFactory(String factoryCode) {

        try {
            return factoryClient.getFactoryByCode(factoryCode);

        } catch (FeignException.NotFound ex) {
            throw new FactoryNotFoundException(
                    "Factory not found with code: " + factoryCode
            );

        }
    }

    private FactoryResponse factoryServiceFallback(
            String factoryCode,
            Throwable throwable) {

        throw new FactoryServiceUnavailableException(
                "Factory service is unavailable"
        );
    }
}