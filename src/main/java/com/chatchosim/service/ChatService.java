package com.chatchosim.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ChatService {

    // 현재 uvicorn을 5001에서 띄우는 기준
    private static final String PYTHON_AI_GENERATE_URL = "http://127.0.0.1:5001/generate";
    private static final String PYTHON_AI_HEALTH_URL = "http://127.0.0.1:5001/health";

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
                    PYTHON_AI_GENERATE_URL,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<?, ?> body = response.getBody();

            if (body == null) {
                return "AI 서버 응답 본문이 비어 있다.";
            }

            Object successObj = body.get("success");
            boolean success = Boolean.TRUE.equals(successObj);

            Object replyObj = body.get("reply");
            String reply = replyObj == null ? "" : String.valueOf(replyObj).trim();

            if (success && !reply.isEmpty()) {
                return reply;
            }

            if (!reply.isEmpty()) {
                return reply;
            }

            return "AI 서버 응답이 비정상이다.";
        } catch (Exception e) {
            return "AI 서버 연결에 실패했다: " + e.getMessage();
        }
    }

    public Map<String, Object> getAiHealth() {
        Map<String, Object> result = new LinkedHashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(
                    PYTHON_AI_HEALTH_URL,
                    HttpMethod.GET,
                    null,
                    Map.class
            );

            Map<?, ?> body = response.getBody();

            result.put("success", true);
            result.put("pythonAiReachable", true);
            result.put("pythonAiHealth", body);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("pythonAiReachable", false);
            result.put("message", "Python AI 서버 상태 확인 실패: " + e.getMessage());
            return result;
        }
    }
}