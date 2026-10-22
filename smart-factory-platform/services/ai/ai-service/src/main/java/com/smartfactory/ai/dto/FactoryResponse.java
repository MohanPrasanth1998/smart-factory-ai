package com.smartfactory.ai.dto;

import com.smartfactory.ai.util.FactoryStatus;

import java.time.LocalDateTime;

public record FactoryResponse(

 Long id,
 String code,
 String name,
 String location,
 FactoryStatus status,
 LocalDateTime createdAt,
 LocalDateTime updatedAt
) {
}
