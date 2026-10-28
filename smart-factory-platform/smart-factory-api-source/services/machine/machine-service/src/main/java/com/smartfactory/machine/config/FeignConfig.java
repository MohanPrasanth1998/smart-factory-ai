package com.smartfactory.machine.config;

import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

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

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }
}