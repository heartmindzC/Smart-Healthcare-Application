package com.example.notificationservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Client để gọi User Service.
 * Dùng để lấy email và fullname của user khi cần gửi notification.
 */
@Slf4j
@Component
public class UserServiceClient {

    @Value("${services.user.url:http://user-service:8080}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Lấy email của user từ user-service.
     *
     * @param userId ID của user
     * @return Optional chứa email nếu tìm thấy
     */
    public Optional<String> getUserEmail(String userId) {
        if (userId == null || userId.isBlank()) {
            log.warn("[UserServiceClient] userId is null or blank");
            return Optional.empty();
        }

        try {
            String url = userServiceUrl + "/users/" + userId + "/email";
            log.debug("[UserServiceClient] Calling user-service to get email for userId: {}", userId);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("result")) {
                @SuppressWarnings("unchecked")
                Map<String, String> result = (Map<String, String>) response.get("result");
                String email = result.get("email");
                log.info("[UserServiceClient] Found email for userId {}: {}", userId, email);
                return Optional.ofNullable(email);
            }
        } catch (Exception e) {
            log.error("[UserServiceClient] Failed to get email for userId {}: {}", userId, e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Lấy fullname của user từ user-service.
     *
     * @param userId ID của user
     * @return Optional chứa fullname nếu tìm thấy
     */
    public Optional<String> getUserFullname(String userId) {
        if (userId == null || userId.isBlank()) {
            return Optional.empty();
        }

        try {
            String url = userServiceUrl + "/users/" + userId + "/email";

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("result")) {
                @SuppressWarnings("unchecked")
                Map<String, String> result = (Map<String, String>) response.get("result");
                return Optional.ofNullable(result.get("fullname"));
            }
        } catch (Exception e) {
            log.error("[UserServiceClient] Failed to get fullname for userId {}: {}", userId, e.getMessage());
        }

        return Optional.empty();
    }

    /**
     * Lấy cả email và fullname của user.
     *
     * @param userId ID của user
     * @return Map chứa email và fullname
     */
    public Map<String, String> getUserInfo(String userId) {
        Map<String, String> userInfo = new HashMap<>();

        getUserEmail(userId).ifPresent(email -> userInfo.put("email", email));
        getUserFullname(userId).ifPresent(fullname -> userInfo.put("fullname", fullname));

        return userInfo;
    }
}
