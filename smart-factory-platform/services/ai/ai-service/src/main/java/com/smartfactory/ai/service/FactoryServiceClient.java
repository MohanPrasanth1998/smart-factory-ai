package com.smartfactory.ai.service;

import com.smartfactory.ai.dto.FactoryResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FactoryServiceClient {

    private final RestClient restClient;

    public FactoryServiceClient(RestClient.Builder restClient){
        this.restClient=restClient.baseUrl("${FACTORY_SERVICE_URL:http://localhost:7070}").build();
    }

    public FactoryResponse getFactoryByCode(String code){
        return restClient.get().uri("/api/v1/factories/{code}",code)
                .retrieve()
                .body(FactoryResponse.class);
    }
}
