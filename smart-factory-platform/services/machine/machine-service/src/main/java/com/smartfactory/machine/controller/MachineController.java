package com.smartfactory.machine.controller;

import com.smartfactory.machine.dto.CreateMachineRequest;
import com.smartfactory.machine.dto.MachineResponse;
import com.smartfactory.machine.dto.MachineSearchRequest;
import com.smartfactory.machine.dto.UpdateMachineRequest;
import com.smartfactory.machine.service.MachineService;
import com.smartfactory.machine.util.MachineStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/machines")
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService){
        this.machineService = machineService;
    }

    /**
     * Creates a new machine.
     */
    @PostMapping
    public ResponseEntity<MachineResponse> createMachine(
            @Valid @RequestBody CreateMachineRequest createMachineRequest){

        MachineResponse response =machineService.create(createMachineRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Retrieves machines using pagination.
     *
     * Example:
     * GET /api/v1/machines?page=0&size=10
     *
     * Default:
     * page = 0
     * size = 10
     */
    @GetMapping
    public ResponseEntity<Page<MachineResponse>> getAll(@ModelAttribute MachineSearchRequest machineSearchRequest
            , @RequestParam (defaultValue = "0") int page
            , @RequestParam (defaultValue = "10") int size){

        PageRequest pageRequest = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC,"id"));
        return ResponseEntity.ok(machineService.getAll(machineSearchRequest,pageRequest));
    }

    /**
     * Retrieves a machine by machine code.
     */
    @GetMapping("/{machineCode}")
    public ResponseEntity<MachineResponse> getByMachineCode(
            @PathVariable String machineCode) {

        return ResponseEntity.ok(
                machineService.getByMachineCode(machineCode)
        );
    }


    /**
     * Updates a machine.
     *
     * The machine code is immutable.
     */
    @PutMapping("/{machineCode}")
    public MachineResponse updateMachine(@PathVariable String machineCode, @Valid
                                            @RequestBody UpdateMachineRequest updateMachineRequest){
       return  machineService.update(machineCode,updateMachineRequest);
    }


    /**
     * Deletes a machine by machine code.
     */
    @DeleteMapping("/{machineCode}")
    public ResponseEntity<Void> deleteMachine(@PathVariable String machineCode){
        machineService.delete(machineCode);
        return ResponseEntity.noContent().build();
    }


}
