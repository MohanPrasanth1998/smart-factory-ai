package com.smartfactory.factory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtDecoderConfig {

    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}")
            Resource publicKeyResource) {

        try (InputStream inputStream = publicKeyResource.getInputStream()) {

            String key = new String(inputStream.readAllBytes())
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");

            byte[] keyBytes = Base64.getDecoder().decode(key);

            X509EncodedKeySpec keySpec =
                    new X509EncodedKeySpec(keyBytes);

            PublicKey publicKey =
                    KeyFactory.getInstance("RSA")
                            .generatePublic(keySpec);

            return NimbusJwtDecoder
                    .withPublicKey((RSAPublicKey) publicKey)
                    .build();

        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Failed to load JWT public key", ex);
        }
    }
}
