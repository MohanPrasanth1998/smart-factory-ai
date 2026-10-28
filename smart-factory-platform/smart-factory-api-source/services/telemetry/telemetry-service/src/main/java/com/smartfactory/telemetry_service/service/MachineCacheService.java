package com.smartfactory.telemetry_service.service;

import com.smartfactory.telemetry_service.client.MachineClient;
import com.smartfactory.telemetry_service.dto.MachineCache;
import com.smartfactory.telemetry_service.dto.MachineResponse;
import com.smartfactory.telemetry_service.exception.MachineNotFoundException;
import com.smartfactory.telemetry_service.exception.MachineServiceUnavailableException;
import feign.FeignException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Service
public class MachineCacheService {

    private static final Duration CACHE_TTL = Duration.ofMinutes(2);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final MachineClient machineClient;


    public MachineCacheService(StringRedisTemplate stringRedisTemplate,ObjectMapper objectMapper
    ,MachineClient machineClient){
        this.stringRedisTemplate=stringRedisTemplate;
        this.objectMapper=objectMapper;
        this.machineClient=machineClient;
    }

    /*
     * Build the Redis key.
     * Check Redis for an existing cached machine value.
     * If the value exists, deserialize it and return it.
     * If the value does not exist, fetch the machine from Machine Service.
     * Map the response to MachineCache, store it in Redis, and return it.
     */

    public MachineCache getMachine(String machineCode){

        String key = buildKey(machineCode);
        String cachedValue;

        try {
            cachedValue = stringRedisTemplate.opsForValue().get(key);
        } catch (RedisConnectionFailureException ex) {
            return fetchMachineCache(machineCode);
        }
        if(cachedValue != null){
            return deserialize(cachedValue);
        }

        MachineCache machineCache = fetchMachineCache(machineCode);
        try {
            cache(key, machineCache);
        } catch (RedisConnectionFailureException ex) {
            // Cache storage failed, but machine details are available.
        }

        return machineCache;

    }

    /*
     * Fetch machine information from Machine Service using OpenFeign.
     */
    private MachineResponse fetchMachine(String machineCode) {
        try {
            return machineClient.getMachineByCode(machineCode);
        } catch (FeignException.NotFound ex) {
            throw new MachineNotFoundException(
                    "Machine not found for machine code "
                            + machineCode
            );
        } catch (FeignException ex) {
            throw new MachineServiceUnavailableException(
                    "Machine service is unavailable",
                    ex
            );
        }
    }


    /*
     * Serialize MachineCache to JSON because Redis stores the value as a string.
     */
    private void cache(String key, MachineCache machineCache) {
        try {
            String json = objectMapper.writeValueAsString(machineCache);
            stringRedisTemplate.opsForValue().set(key, json, CACHE_TTL);
        } catch (JacksonException ex) {
            throw new IllegalStateException("Failed to serialize machine cache", ex);
        }
    }


    /*
     * Deserialize the cached JSON value back into MachineCache.
     */
    private MachineCache deserialize(String json){
        try{
            return  objectMapper.readValue(json,MachineCache.class);
        } catch (JacksonException ex) {
            throw new IllegalArgumentException("Failed to deserialize machine cache",ex);
        }
    }

    /*
     * Create the Redis key for the machine cache.
     */
    private String buildKey(String machineCode){
        return "machine:"+ machineCode;
    }

    private MachineCache fetchMachineCache(String machineCode) {
        MachineResponse response = fetchMachine(machineCode);

        return new MachineCache(
                response.machineCode(),
                response.name(),
                response.factoryCode(),
                response.status()
        );
    }

    private void invalidateMachine(String machineCode){
        String key = buildKey(machineCode);
        stringRedisTemplate.delete(key);
    }
}
