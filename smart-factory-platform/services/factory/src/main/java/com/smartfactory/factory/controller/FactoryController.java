package com.smartfactory.factory.controller;

import com.smartfactory.factory.dto.CreateFactoryRequest;
import com.smartfactory.factory.dto.FactoryResponse;
import com.smartfactory.factory.dto.PageResponse;
import com.smartfactory.factory.dto.UpdateFactoryRequest;
import com.smartfactory.factory.exception.InvalidPaginationException;
import com.smartfactory.factory.service.FactoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/factories")
@RequiredArgsConstructor
public class FactoryController {

    private final FactoryService factoryService;

    @PostMapping()
    public ResponseEntity<FactoryResponse> createFactory(@Valid @RequestBody CreateFactoryRequest factoryRequest){

        FactoryResponse factoryResponse = factoryService.createFactory(factoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(factoryResponse);
    }


    @GetMapping("/{code}")
    public ResponseEntity<FactoryResponse> getFactoryByCode(@PathVariable (name="code") String code){
       FactoryResponse factoryResponse = factoryService.getFactoryByCode(code);
        return  ResponseEntity.status(HttpStatus.OK).body(factoryResponse);
    }

    @GetMapping()
    public ResponseEntity<PageResponse<FactoryResponse>> getAllFactories(@RequestParam (defaultValue = "0") int page,@RequestParam (defaultValue = "3") int size){

        if(page < 0){
           throw new InvalidPaginationException( "Page must be greater than or equal to 0");
       }

        if(size < 1 || size > 100 ){
            throw new InvalidPaginationException("Size must be between 1 and 100");
        }

        PageResponse<FactoryResponse> factoryResponse = factoryService.getAllFactories(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(factoryResponse);
    }

    @PutMapping("/{code}")
    public ResponseEntity<FactoryResponse> updateFactory(@PathVariable String code, @Valid @RequestBody UpdateFactoryRequest factoryRequest){
        FactoryResponse factoryResponse = factoryService.updateFactoryByCode(code, factoryRequest);
        return ResponseEntity.status(HttpStatus.OK).body(factoryResponse);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteFactory(@PathVariable String code){
        factoryService.deleteFactoryByCode(code);
        return ResponseEntity.noContent().build();
    }

}


