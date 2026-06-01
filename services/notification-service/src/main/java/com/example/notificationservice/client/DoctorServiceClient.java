package com.example.notificationservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client để gọi Doctor Service.
 * Dùng để lấy userId của một bác sĩ theo doctorId khi cần gửi notification.
 */
@Slf4j
@Component
public class DoctorServiceClient {

    @Value("${services.doctor.url:http://doctor-service:8082}")
    private String doctorServiceUrl;

    private final RestTemplate restTemplate;

    public DoctorServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Lấy userId của bác sĩ từ doctor-service theo doctorId.
     *
     * @param doctorId ID của bác sĩ (doctorId)
     * @return userId nếu tìm thấy, chuỗi rỗng nếu không tìm thấy hoặc có lỗi
     */
    public String getUserIdByDoctorId(String doctorId) {
        if (doctorId == null || doctorId.isBlank()) {
            log.warn("[DoctorServiceClient] doctorId is null or blank");
            return "";
        }

        try {
            String url = doctorServiceUrl + "/doctors/" + doctorId;
            log.debug("[DoctorServiceClient] Calling doctor-service to get userId for doctorId: {}", doctorId);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("result")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> result = (Map<String, Object>) response.get("result");
                String userId = (String) result.get("userId");
                log.info("[DoctorServiceClient] Found userId for doctorId {}: {}", doctorId, userId);
                return userId != null ? userId : "";
            }
        } catch (Exception e) {
            log.error("[DoctorServiceClient] Failed to get userId for doctorId {}: {}", doctorId, e.getMessage());
        }

        return "";
    }
}
