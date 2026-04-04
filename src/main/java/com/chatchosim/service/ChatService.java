package com.chatchosim.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ChatService {

    @Value("${CHOSIM_AI_GENERATE_URL}")
    private String pythonAiGenerateUrl;

    @Value("${CHOSIM_AI_HEALTH_URL}")
    private String pythonAiHealthUrl;

    public String generateReply(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "메시지가 비어 있다.";
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("message", message.trim());
            requestBody.put("max_new_tokens", 120);
            requestBody.put("temperature", 0.8);
            requestBody.put("top_p", 0.95);
            requestBody.put("repetition_penalty", 1.1);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    pythonAiGenerateUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return "AI 응답을 불러오지 못했다.";
            }

            Object success = response.getBody().get("success");
            Object reply = response.getBody().get("reply");

            if (success instanceof Boolean && !((Boolean) success)) {
                return reply != null ? reply.toString() : "AI 서버가 응답을 거부했다.";
            }

            return reply != null ? reply.toString() : "응답이 비어 있다.";

        } catch (Exception e) {
            return "서버 처리 중 오류: " + e.getMessage();
        }
    }

    public boolean checkPythonHealth() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(pythonAiHealthUrl, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getAiHealth() {
        return checkPythonHealth();
    }
}