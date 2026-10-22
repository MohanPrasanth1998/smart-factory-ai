package com.smartfactory.gateway.config;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {

        ClassPathResource resource =
                new ClassPathResource("keys/jwt-public.pem");

        String pem;

        try (InputStream inputStream = resource.getInputStream()) {
            pem = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        String publicKeyContent = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decodedKey =
                Base64.getDecoder().decode(publicKeyContent);

        X509EncodedKeySpec keySpec =
                new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory =
                KeyFactory.getInstance("RSA");

        RSAPublicKey publicKey =
                (RSAPublicKey) keyFactory.generatePublic(keySpec);

        return NimbusJwtDecoder
                .withPublicKey(publicKey)
                .build();
    }
}