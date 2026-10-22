package com.smartfactory.factory.service;

import com.smartfactory.factory.dto.CreateFactoryRequest;
import com.smartfactory.factory.dto.FactoryResponse;
import com.smartfactory.factory.dto.PageResponse;
import com.smartfactory.factory.dto.UpdateFactoryRequest;

import java.util.List;

public interface FactoryService {

    FactoryResponse createFactory(CreateFactoryRequest factoryRequest);
    FactoryResponse getFactoryByCode(String code);
    PageResponse<FactoryResponse> getAllFactories(int page, int size);
    FactoryResponse updateFactoryByCode(String code, UpdateFactoryRequest factoryRequest);

    void deleteFactoryByCode(String code);
}
