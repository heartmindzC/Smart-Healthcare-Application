package com.example.doctorservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client để gọi Appointment Service
 * Dùng để query availability từ Appointment (QUERY-DRIVEN AVAILABILITY)
 */
@Component
public class AppointmentServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServiceClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.appointment.url:http://host.docker.internal:8083}")
    private String appointmentServiceUrl;

    /**
     * Kiểm tra slot có available không dựa trên CONFIRMED appointments
     * Logic: Slot IS available KHI và CHỈ KHI không có CONFIRMED appointment trùng doctor + slot
     * 
     * @param doctorId ID của bác sĩ
     * @param timeSlotId ID của time slot
     * @return true nếu available (không có CONFIRMED), false nếu đã có người confirmed
     */
    public boolean isSlotAvailable(String doctorId, String timeSlotId) {
        try {
            String url = appointmentServiceUrl + "/appointments/check-availability?doctorId=" + doctorId + "&timeSlotId=" + timeSlotId;
            logger.info("Checking slot availability: doctor={}, slot={}, url={}", doctorId, timeSlotId, url);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("available")) {
                Object available = response.getBody().get("available");
                boolean isAvailable = Boolean.TRUE.equals(available);
                logger.info("Slot availability for doctor={}, slot={}: {}", doctorId, timeSlotId, isAvailable);
                return isAvailable;
            }

            logger.warn("Could not parse availability response, assuming NOT available");
            return false;

        } catch (Exception e) {
            logger.error("Failed to check slot availability: doctor={}, slot={}, error={}", 
                    doctorId, timeSlotId, e.getMessage());
            return false;
        }
    }

    /**
     * Đếm số CONFIRMED appointments trùng doctor + slot
     * 
     * @param doctorId ID của bác sĩ
     * @param timeSlotId ID của time slot
     * @return số lượng CONFIRMED appointments (0 hoặc 1, vì 1 slot chỉ có 1 người)
     */
    public long countConfirmedAppointments(String doctorId, String timeSlotId) {
        try {
            String url = appointmentServiceUrl + "/appointments/count-confirmed?doctorId=" + doctorId + "&timeSlotId=" + timeSlotId;
            logger.info("Counting confirmed appointments: doctor={}, slot={}", doctorId, timeSlotId);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("count")) {
                Object count = response.getBody().get("count");
                if (count instanceof Number) {
                    return ((Number) count).longValue();
                }
            }

            return 0L;

        } catch (Exception e) {
            logger.error("Failed to count confirmed appointments: doctor={}, slot={}, error={}", 
                    doctorId, timeSlotId, e.getMessage());
            return 0L;
        }
    }
}
