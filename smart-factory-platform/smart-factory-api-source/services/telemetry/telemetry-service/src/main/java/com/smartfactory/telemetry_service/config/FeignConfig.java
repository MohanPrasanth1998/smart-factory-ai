package com.smartfactory.telemetry_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import feign.RequestInterceptor;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor bearerTokenInterceptor() {

        return requestTemplate -> {

            var authentication =
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {

                String token =
                        jwtAuthenticationToken
                                .getToken()
                                .getTokenValue();

                requestTemplate.header(
                        "Authorization",
                        "Bearer " + token
                );
            }
        };
    }
}