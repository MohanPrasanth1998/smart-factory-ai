package com.smartfactory.telemetry_service.service;

import com.smartfactory.telemetry_service.client.MachineClient;
import com.smartfactory.telemetry_service.dto.*;
import com.smartfactory.telemetry_service.entity.Telemetry;

import com.smartfactory.telemetry_service.exception.TelemetryNotFoundException;
import com.smartfactory.telemetry_service.repository.TelemetryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class TelemetryService {

    private final TelemetryRepository telemetryRepository;
    private final MachineClient machineClient;
    private final MachineCacheService machineCacheService;
    private final TelemetryEventPublisher telemetryEventPublisher;


    public TelemetryService(TelemetryRepository telemetryRepository, MachineClient machineClient, MachineCacheService machineCacheService, TelemetryEventPublisher telemetryEventPublisher){
        this.telemetryRepository=telemetryRepository;
        this.machineClient= machineClient;
        this.machineCacheService = machineCacheService;
        this.telemetryEventPublisher = telemetryEventPublisher;
    }


    @Transactional
    public TelemetryResponse create(CreateTelemetryRequest createTelemetryRequest){
        validateMachine(createTelemetryRequest.machineCode());
        Telemetry telemetry = new Telemetry();
        telemetry.setMachineCode(createTelemetryRequest.machineCode());
        telemetry.setTimestamp(createTelemetryRequest.timestamp());
        telemetry.setMachineState(createTelemetryRequest.machineState());
        telemetry.setMetrics(createTelemetryRequest.metrics());
        telemetry.setCreatedAt(Instant.now());

        Telemetry savedTelemetry =telemetryRepository.save(telemetry);
        telemetryEventPublisher.publish(savedTelemetry);

        return toResponse(savedTelemetry);
    }

    @Transactional(readOnly = true)
    public TelemetryResponse getTelemetry(Long id){
        Telemetry telemetry =telemetryRepository.findById(id).orElseThrow(()->
                new TelemetryNotFoundException("Telemetry not found with id "+ id));
        return toResponse(telemetry);
    }

    @Transactional(readOnly = true)
    public TelemetryPageResponse getMachineTelemetry(String machineCode, String cursor,int limit){
        validateLimit(limit);
        int fetchSize = limit+1;
        List<Telemetry> telemetry;

        if(cursor == null || cursor.isBlank()){
            telemetry =telemetryRepository.findFirstTelemetry(machineCode, PageRequest.of(0,fetchSize));
        } else {
            TelemetryCursor decodedCursor = decodeCursor(cursor);
            telemetry = telemetryRepository.findNextTelemetry(machineCode,decodedCursor.timestamp(),
                    decodedCursor.id(),PageRequest.of(0,fetchSize));
        }

        boolean hasNext = telemetry.size()>limit;

        if(hasNext){
           telemetry = telemetry.subList(0, limit);
        }

        String nextCursor = null;

        if (hasNext) {
            Telemetry lastTelemetry = telemetry.get(telemetry.size() - 1);

            nextCursor = encodeCursor(
                            lastTelemetry.getTimestamp(),
                            lastTelemetry.getId()
                    );
        }

        List<TelemetryResponse> content = telemetry.stream()
                        .map(this::toResponse)
                        .toList();

        return new TelemetryPageResponse(
                content,
                nextCursor,
                hasNext);
    }


    private void validateLimit(int limit){
        if(limit < 1 || limit > 100){
            throw new IllegalArgumentException("Limit must be between 1 to 100");
        }
    }

    private  String encodeCursor(Instant timestamp,Long id){
        return timestamp+"_"+id;
    }

    private TelemetryCursor decodeCursor(String cursor) {
        int separatorIndex = cursor.lastIndexOf('_');

        if (separatorIndex <= 0 || separatorIndex == cursor.length() - 1) {
            throw new IllegalArgumentException("Invalid cursor");
        }

        String timestampPart =
                cursor.substring(0, separatorIndex);

        String idPart =
                cursor.substring(separatorIndex + 1);

        try {
            Instant timestamp = Instant.parse(timestampPart);
            Long id = Long.parseLong(idPart);

            return new TelemetryCursor(
                    timestamp,
                    id
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid cursor");
        }
    }


    private void validateMachine(String machineCode){
        machineCacheService.getMachine(machineCode);
    }

    private TelemetryResponse toResponse(Telemetry telemetry){
        return new TelemetryResponse(
                telemetry.getId(),
                telemetry.getMachineCode(),
                telemetry.getTimestamp(),
                telemetry.getMachineState(),
                telemetry.getMetrics(),
                telemetry.getCreatedAt()
        );
    }
}
