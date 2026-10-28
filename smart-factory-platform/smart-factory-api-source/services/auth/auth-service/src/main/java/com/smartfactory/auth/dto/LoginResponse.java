package com.smartfactory.auth.dto;

public record LoginResponse(

        String token,
        String tokenType
) {
}
