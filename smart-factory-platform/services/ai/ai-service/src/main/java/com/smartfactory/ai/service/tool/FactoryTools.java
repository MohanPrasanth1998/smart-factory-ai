package com.smartfactory.ai.service.tool;


import com.smartfactory.ai.dto.FactoryResponse;
import com.smartfactory.ai.service.FactoryServiceClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class FactoryTools {

    private final FactoryServiceClient factoryServiceClient;

    public FactoryTools(FactoryServiceClient factoryServiceClient) {
        this.factoryServiceClient = factoryServiceClient;
    }

    @Tool(description = """
        Retrieve the REAL factory information from the Factory Service.

        IMPORTANT:
        Use this tool whenever the user provides a factory code
        such as FAC-2UCVMW and asks for information about that factory.

        Do NOT answer factory details from your own knowledge.
        Do NOT invent factory names, locations, statuses, or IDs.

        The factory code must be passed exactly as provided by the user.
        """)
    public FactoryResponse getFactoryByCode(@ToolParam(description = "The unique factory code") String code) {
        return  factoryServiceClient.getFactoryByCode(code);
    }

}
