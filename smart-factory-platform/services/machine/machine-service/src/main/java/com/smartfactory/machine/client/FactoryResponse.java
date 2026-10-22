package com.smartfactory.machine.client;

public record FactoryResponse(
        String factoryCode,
        String name,
        String status
) {
}
