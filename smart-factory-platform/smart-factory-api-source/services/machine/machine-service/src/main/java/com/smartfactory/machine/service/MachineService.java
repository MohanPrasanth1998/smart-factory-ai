package com.smartfactory.machine.service;

import com.smartfactory.machine.client.FactoryClient;
import com.smartfactory.machine.client.FactoryResponse;
import com.smartfactory.machine.dto.CreateMachineRequest;
import com.smartfactory.machine.dto.MachineResponse;
import com.smartfactory.machine.dto.MachineSearchRequest;
import com.smartfactory.machine.dto.UpdateMachineRequest;
import com.smartfactory.machine.entity.Machine;

import com.smartfactory.machine.exception.MachineNotFoundException;
import com.smartfactory.machine.repository.MachineRepository;
import com.smartfactory.machine.specification.MachineSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class MachineService {

    private static final String MACHINE_CODE_PREFIX = "MCH-";
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final FactoryClientService factoryClientService;
    private final MachineRepository machineRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public MachineService( FactoryClientService factoryClientService, MachineRepository machineRepository) {
        this.factoryClientService = factoryClientService;
        this.machineRepository = machineRepository;
    }

    @Transactional
    public MachineResponse create(CreateMachineRequest request) {
       FactoryResponse factoryResponse= factoryClientService.getFactory(request.factoryCode());

        String machineCode = generateUniqueMachineCode();

        LocalDateTime now = LocalDateTime.now();

        Machine machine = new Machine();

        machine.setMachineCode(machineCode);
        machine.setName(request.name());
        machine.setType(request.type());
        machine.setFactoryCode(factoryResponse.factoryCode());
        machine.setStatus(request.status());
        machine.setCreatedAt(now);
        machine.setUpdatedAt(now);

        Machine savedMachine = machineRepository.save(machine);

        return toResponse(savedMachine);
    }

    public Page<MachineResponse> getAll(MachineSearchRequest machineSearchRequest, Pageable pageable) {

        Specification<Machine> specification = null;

        if (machineSearchRequest != null && machineSearchRequest.status() != null) {

            specification = MachineSpecification.hasStatus(machineSearchRequest.status());
        }

        if (machineSearchRequest != null && StringUtils.hasText(machineSearchRequest.factoryCode())) {

            Specification<Machine> factorySpecification = MachineSpecification.hasFactoryCode(machineSearchRequest.factoryCode().trim());

            if (specification == null) {
                specification = factorySpecification;
            } else {
                specification = specification.and(factorySpecification);
            }
        }

        if (machineSearchRequest != null && StringUtils.hasText(machineSearchRequest.type())) {

            Specification<Machine> typeSpecification = MachineSpecification.hasType(machineSearchRequest.type().trim());

            if (specification == null) {
                specification = typeSpecification;
            } else {
                specification = specification.and(typeSpecification);
            }
        }

        if (machineSearchRequest != null && StringUtils.hasText(machineSearchRequest.search())) {

            Specification<Machine> searchSpecification = MachineSpecification.search(machineSearchRequest.search().trim());

            if (specification == null) {
                specification = searchSpecification;
            } else {
                specification = specification.and(searchSpecification);
            }
        }

        if (specification == null) {
            return machineRepository.findAll(pageable).map(this::toResponse);
        }

        return machineRepository.findAll(specification, pageable).map(this::toResponse);
    }

    public MachineResponse getByMachineCode(String machineCode) {

        Machine machine = machineRepository.findByMachineCode(machineCode).orElseThrow(() -> new MachineNotFoundException("Machine not found with code: " + machineCode));

        return toResponse(machine);
    }

    @Transactional
    public MachineResponse update(String machineCode, UpdateMachineRequest request) {

        Machine machine = machineRepository.findByMachineCode(machineCode).orElseThrow(() -> new MachineNotFoundException("Machine not found with code: " + machineCode));

        machine.setName(request.name());
        machine.setType(request.type());
        machine.setFactoryCode(request.factoryCode());
        machine.setStatus(request.status());
        machine.setUpdatedAt(LocalDateTime.now());

        return toResponse(machine);
    }

    @Transactional
    public void delete(String machineCode) {

        Machine machine = machineRepository.findByMachineCode(machineCode).orElseThrow(() -> new MachineNotFoundException("Machine not found: " + machineCode));

        machineRepository.delete(machine);
    }

    private String generateUniqueMachineCode() {

        String machineCode;

        do {
            machineCode = MACHINE_CODE_PREFIX + randomAlphaNumeric();

        } while (machineRepository.existsByMachineCode(machineCode));

        return machineCode;
    }

    private String randomAlphaNumeric() {

        StringBuilder value = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            value.append(ALPHANUMERIC.charAt(secureRandom.nextInt(ALPHANUMERIC.length())));
        }

        return value.toString();
    }

    private MachineResponse toResponse(Machine machine) {

        return new MachineResponse(machine.getId(), machine.getMachineCode(), machine.getName(), machine.getType(), machine.getFactoryCode(), machine.getStatus(), machine.getCreatedAt(), machine.getUpdatedAt());
    }
}