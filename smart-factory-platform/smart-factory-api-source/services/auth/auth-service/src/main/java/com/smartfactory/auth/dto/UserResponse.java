package com.smartfactory.auth.dto;

import com.smartfactory.auth.util.UserRole;

import java.time.LocalDateTime;

public record UserResponse(

        Long id,
        String username,
        UserRole role,
        boolean enabled,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
