package com.smartfactory.telemetry_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void shouldConnectToRedis(){
        String key = "test:redis";
        String value = "connected";

        redisTemplate.opsForValue().set(key,value, Duration.ofSeconds(60));
        String result = redisTemplate.opsForValue().get(key);

        assertEquals(value, result);

    }
}
