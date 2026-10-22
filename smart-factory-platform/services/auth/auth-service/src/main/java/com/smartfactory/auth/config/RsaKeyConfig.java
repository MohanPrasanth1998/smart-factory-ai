package com.smartfactory.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class RsaKeyConfig {

    @Bean
    public KeyPair keyPair(
            @Value("${jwt.private-key}") String privateKeyPath,
            @Value("${jwt.public-key}") String publicKeyPath) {

        try {
            PrivateKey privateKey = loadPrivateKey(privateKeyPath);
            PublicKey publicKey = loadPublicKey(publicKeyPath);

            return new KeyPair(publicKey, privateKey);

        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Failed to load RSA key pair", ex
            );
        }
    }

    private PrivateKey loadPrivateKey(String path)
            throws Exception {

        String key = Files.readString(Path.of(path))
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec keySpec =
                new PKCS8EncodedKeySpec(keyBytes);

        return KeyFactory
                .getInstance("RSA")
                .generatePrivate(keySpec);
    }

    private PublicKey loadPublicKey(String path)
            throws Exception {

        String key = Files.readString(Path.of(path))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);

        X509EncodedKeySpec keySpec =
                new X509EncodedKeySpec(keyBytes);

        return KeyFactory
                .getInstance("RSA")
                .generatePublic(keySpec);
    }
}