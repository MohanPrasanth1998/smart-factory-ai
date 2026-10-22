package com.smartfactory.telemetry_service;

import com.smartfactory.telemetry_service.client.MachineClient;
import com.smartfactory.telemetry_service.dto.MachineCache;
import com.smartfactory.telemetry_service.dto.MachineResponse;
import com.smartfactory.telemetry_service.service.MachineCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MachineCacheServiceTest {

    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOperations;
    private MachineClient machineClient;
    private MachineCacheService machineCacheService;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        machineClient = mock(MachineClient.class);

        when(redisTemplate.opsForValue())
                .thenReturn(valueOperations);

        machineCacheService =
                new MachineCacheService(
                        redisTemplate,
                        new ObjectMapper(),
                        machineClient
                );
    }

    @Test
    void shouldFetchMachineFromServiceAndCacheIt() {
        String machineCode = "MCH-SMEFRB";

        MachineResponse machineResponse =
                new MachineResponse(
                        1L,
                        machineCode,
                        "CNC Machine",
                        "CNC",
                        "FAC-TEST01",
                        "ACTIVE"
                );

        when(valueOperations.get(
                "machine:" + machineCode
        )).thenReturn(null);

        when(machineClient.getMachineByCode(machineCode))
                .thenReturn(machineResponse);

        MachineCache result =
                machineCacheService.getMachine(machineCode);

        assertEquals(
                machineCode,
                result.machineCode()
        );

        assertEquals(
                "CNC Machine",
                result.name()
        );

        verify(machineClient, times(1))
                .getMachineByCode(machineCode);
        verify(valueOperations, times(1))
                .set(
                        eq("machine:" + machineCode),
                        anyString(),
                        eq(Duration.ofMinutes(2))
                );
    }

    @Test
    void shouldReturnMachineFromCacheWithoutCallingMachineService() {

        String machineCode = "MCH-SMEFRB";
        String redisKey = "machine:" + machineCode;

        String cachedJson = """
            {
              "machineCode": "MCH-SMEFRB",
              "name": "Cached CNC Machine",
              "factoryCode": "FAC-TEST01",
              "status": "ACTIVE"
            }
            """;

        when(valueOperations.get(redisKey))
                .thenReturn(cachedJson);

        MachineCache result =
                machineCacheService.getMachine(machineCode);

        assertEquals(
                machineCode,
                result.machineCode()
        );

        assertEquals(
                "Cached CNC Machine",
                result.name()
        );

        assertEquals(
                "FAC-TEST01",
                result.factoryCode()
        );

        assertEquals(
                "ACTIVE",
                result.status()
        );

        verify(machineClient, never())
                .getMachineByCode(machineCode);

        verify(valueOperations, never())
                .set(
                        anyString(),
                        anyString(),
                        any(Duration.class)
                );
    }
}