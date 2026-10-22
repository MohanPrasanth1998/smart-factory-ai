package com.smartfactory.factory.service;

import com.smartfactory.factory.dto.CreateFactoryRequest;
import com.smartfactory.factory.dto.FactoryResponse;
import com.smartfactory.factory.dto.PageResponse;
import com.smartfactory.factory.dto.UpdateFactoryRequest;
import com.smartfactory.factory.entity.Factory;
import com.smartfactory.factory.exception.FactoryNotFoundException;
import com.smartfactory.factory.repository.FactoryRepository;
import com.smartfactory.factory.util.FactoryCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class FactoryServiceImpl implements FactoryService{


    private final FactoryRepository factoryRepository;

    private final FactoryCodeGenerator factoryCodeGenerator;

    public FactoryServiceImpl(FactoryRepository factoryRepository, FactoryCodeGenerator factoryCodeGenerator){
        this.factoryRepository= factoryRepository;
        this.factoryCodeGenerator = factoryCodeGenerator;
    }


    @Transactional
    @Override
    public FactoryResponse createFactory(CreateFactoryRequest factoryRequest) {


        Factory factory = new Factory();
        String code =generateUniqueFactoryCode();
        factory.setCode(code);
        factory.setName(factoryRequest.name());
        factory.setLocation(factoryRequest.location());
        factory.setStatus(factoryRequest.status());

        LocalDateTime now = LocalDateTime.now();
        factory.setCreatedAt(now);
        factory.setUpdatedAt(now);

        Factory savedFactory = factoryRepository.save(factory);
        log.info(" Factory created successfully [code={},name={}]",code,savedFactory.getName());
        return new FactoryResponse(
                savedFactory.getId(),
                savedFactory.getName(),
                savedFactory.getCode(),
                savedFactory.getLocation(),
                savedFactory.getStatus(),
                savedFactory.getCreatedAt(),
                savedFactory.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    @Override
    public FactoryResponse getFactoryByCode(String code) {

        Factory factory = factoryRepository.findByCode(code)
                .orElseThrow(() ->
                        new FactoryNotFoundException("Factory with code '" + code + "' not found"));

        return new FactoryResponse(
                factory.getId(),
                factory.getCode(),
                factory.getName(),
                factory.getLocation(),
                factory.getStatus(),
                factory.getCreatedAt(),
                factory.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<FactoryResponse> getAllFactories(int page, int size) {

        List<Factory> factories = factoryRepository.findFactories(page,size);

        long totalElements =factoryRepository.countFactories();

        List<FactoryResponse> content = factories.stream()
                .map(factory -> new FactoryResponse(
                        factory.getId(),
                        factory.getCode(),
                        factory.getLocation(),
                        factory.getName(),
                        factory.getStatus(),
                        factory.getCreatedAt(),
                        factory.getUpdatedAt()
                )).toList();

        int totalPages = (int) Math.ceil((double) totalElements/size);
        boolean hasNext = page+1 < totalPages;
        boolean hasPrevious = page > 0;

        return new PageResponse<>(
                content,
                page,
                size,
                totalElements,
                totalPages,
                hasNext,
                hasPrevious

        );
    }


    @Transactional
    @Override
    public FactoryResponse updateFactoryByCode(String code, UpdateFactoryRequest factoryRequest){

        Factory factory = factoryRepository.findByCode(code)
                .orElseThrow(() -> new FactoryNotFoundException("Factory not found with code: " + code));

        factory.setName(factoryRequest.name());
        factory.setLocation(factoryRequest.location());
        factory.setStatus(factoryRequest.status());

        Factory updatedFactory = factoryRepository.save(factory);
        log.info("Factory Updated Successfully name={},code={}",updatedFactory.getName(),code);
        return new FactoryResponse(
                updatedFactory.getId(),updatedFactory.getCode(),updatedFactory.getName(),
                updatedFactory.getLocation(),updatedFactory.getStatus(),
                updatedFactory.getCreatedAt(),updatedFactory.getUpdatedAt());
   }


   @Transactional
   @Override
   public void deleteFactoryByCode(String code){
        Factory factory = factoryRepository.findByCode(code)
                .orElseThrow(()-> new FactoryNotFoundException("Factory not found with code: " + code));
        factoryRepository.delete(factory);
       log.info("Factory deleted successfully [code={}]", code);
   }


    private String generateUniqueFactoryCode() {

        String code;

        do {
            code = factoryCodeGenerator.generate();
        } while (factoryRepository.existsByCode(code));

        return code;
    }
}
