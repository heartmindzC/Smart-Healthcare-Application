package com.example.notificationservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client để gọi Patient Service.
 * Dùng để lấy userId của một bệnh nhân theo patientId khi cần gửi notification.
 */
@Slf4j
@Component
public class PatientServiceClient {

    @Value("${services.patient.url:http://patient-service:8081}")
    private String patientServiceUrl;

    private final RestTemplate restTemplate;

    public PatientServiceClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Lấy userId của bệnh nhân từ patient-service theo patientId.
     *
     * @param patientId ID của bệnh nhân (patientId)
     * @return userId nếu tìm thấy, chuỗi rỗng nếu không tìm thấy hoặc có lỗi
     */
    public String getUserIdByPatientId(String patientId) {
        if (patientId == null || patientId.isBlank()) {
            log.warn("[PatientServiceClient] patientId is null or blank");
            return "";
        }

        try {
            String url = patientServiceUrl + "/patients/" + patientId;
            log.debug("[PatientServiceClient] Calling patient-service to get userId for patientId: {}", patientId);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("result")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> result = (Map<String, Object>) response.get("result");
                String userId = (String) result.get("userId");
                log.info("[PatientServiceClient] Found userId for patientId {}: {}", patientId, userId);
                return userId != null ? userId : "";
            }
        } catch (Exception e) {
            log.error("[PatientServiceClient] Failed to get userId for patientId {}: {}", patientId, e.getMessage());
        }

        return "";
    }
}
