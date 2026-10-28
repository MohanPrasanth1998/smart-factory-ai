package com.smartfactory.ai.controller;

import com.smartfactory.ai.dto.ChatRequest;
import com.smartfactory.ai.dto.AiResponse;
import com.smartfactory.ai.service.AiChatService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiChatService aiChatService;

    public AiController(AiChatService aiChatService){
        this.aiChatService=aiChatService;
    }
//
//    @PostMapping("/chat")
//    public ResponseEntity<AiResponse> createChat(@Valid @RequestBody ChatRequest chatRequest){
//        return ResponseEntity.ok(
//                aiChatService.chat(chatRequest.message())
//        );


    @PostMapping("/chat")
    public ResponseEntity<String> chat(
            @Valid @RequestBody ChatRequest request) {

        return ResponseEntity.ok(
                aiChatService.chat(request.message())
        );
    }
    }

