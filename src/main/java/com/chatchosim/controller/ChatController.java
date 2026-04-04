package com.chatchosim.controller;

import com.chatchosim.dto.ChatRequest;
import com.chatchosim.dto.ChatResponse;
import com.chatchosim.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("status", "UP");
        result.put("service", "ChatChosim");
        result.put("ai", chatService.getAiHealth());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String reply = chatService.generateReply(request.getMessage());
        ChatResponse response = new ChatResponse(true, reply);
        return ResponseEntity.ok(response);
    }
}