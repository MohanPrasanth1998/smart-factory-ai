package com.smartfactory.factory.dto;

import com.smartfactory.factory.entity.FactoryStatus;

import java.time.LocalDateTime;

public record FactoryResponse(

        Long id,
        String factoryCode,
        String name,
        String location,
        FactoryStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        ) {
}
