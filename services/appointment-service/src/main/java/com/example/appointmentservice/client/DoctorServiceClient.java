package com.example.appointmentservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client để gọi Doctor Service thông qua API Gateway/trực tiếp
 * Dùng để quản lý time slot khi appointment thay đổi trạng thái
 */
@Component
public class DoctorServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.doctor.url:http://host.docker.internal:8082}")
    private String doctorServiceUrl;

    /**
     * Giải phóng time slot (mark as available) khi appointment bị hủy hoặc xóa
     * 
     * @param timeSlotId ID của time slot cần giải phóng
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean releaseTimeSlot(String timeSlotId) {
        try {
            String url = doctorServiceUrl + "/time-slots/update-availability/" + timeSlotId + "?isAvailable=true";
            logger.info("Releasing time slot: {} via URL: {}", timeSlotId, url);

            restTemplate.patchForObject(url, null, Void.class);
            logger.info("Successfully released time slot: {}", timeSlotId);
            return true;

        } catch (Exception e) {
            logger.error("Failed to release time slot: {}. Error: {}", timeSlotId, e.getMessage());
            return false;
        }
    }

    /**
     * Đánh dấu time slot là đã đặt (not available) khi appointment được xác nhận
     * 
     * @param timeSlotId ID của time slot cần đánh dấu
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean markTimeSlotAsBooked(String timeSlotId) {
        try {
            String url = doctorServiceUrl + "/time-slots/update-availability/" + timeSlotId + "?isAvailable=false";
            logger.info("Marking time slot as booked: {} via URL: {}", timeSlotId, url);

            restTemplate.patchForObject(url, null, Void.class);
            logger.info("Successfully marked time slot as booked: {}", timeSlotId);
            return true;

        } catch (Exception e) {
            logger.error("Failed to mark time slot as booked: {}. Error: {}", timeSlotId, e.getMessage());
            return false;
        }
    }

    /**
     * Kiểm tra time slot có available không
     * 
     * @param timeSlotId ID của time slot
     * @return true nếu available, false nếu không
     */
    public boolean isTimeSlotAvailable(String timeSlotId) {
        try {
            String url = doctorServiceUrl + "/time-slots/" + timeSlotId;
            logger.info("Checking time slot availability: {} via URL: {}", timeSlotId, url);

            // Gọi API để lấy thông tin time slot
            // Response structure: { result: { isAvailable: true/false } }
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("result")) {
                Object result = response.getBody().get("result");
                if (result instanceof Map) {
                    Object isAvailable = ((Map<?, ?>) result).get("isAvailable");
                    boolean available = Boolean.TRUE.equals(isAvailable);
                    logger.info("Time slot {} availability: {}", timeSlotId, available);
                    return available;
                }
            }

            // Fallback: nếu không parse được, thử kiểm tra lại bằng cách gọi API khác
            // Hoặc coi như available nếu không chắc chắn
            logger.warn("Could not parse time slot availability response, assuming available");
            return true;

        } catch (Exception e) {
            logger.error("Failed to check time slot availability: {}. Error: {}", timeSlotId, e.getMessage());
            return false;
        }
    }

    /**
     * Tạo time slot mới
     * 
     * @param doctorId            ID của bác sĩ
     * @param appointmentDateTime Thời gian hẹn
     * @return ID của time slot được tạo, hoặc null nếu thất bại
     */
    public String createTimeSlot(String doctorId, java.time.LocalDateTime appointmentDateTime) {
        try {
            String url = doctorServiceUrl + "/time-slots/";
            logger.info("Creating time slot for doctor: {} at: {} via URL: {}", doctorId, appointmentDateTime, url);

            Map<String, Object> request = new java.util.HashMap<>();
            request.put("doctorId", doctorId);
            request.put("dayOfWeek", appointmentDateTime.getDayOfWeek().name());
            request.put("startTime", appointmentDateTime.toLocalTime().toString());
            // Giả định mỗi buổi hẹn kéo dài 1 tiếng
            request.put("endTime", appointmentDateTime.toLocalTime().plusHours(1).toString());
            request.put("isAvailable", true);
            request.put("specificDate", appointmentDateTime.toLocalDate().toString());

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("result")) {
                Object result = response.getBody().get("result");
                if (result instanceof Map) {
                    Object timeSlotId = ((Map<?, ?>) result).get("timeSlotId");
                    if (timeSlotId == null) {
                        timeSlotId = ((Map<?, ?>) result).get("id");
                    }
                    if (timeSlotId != null) {
                        logger.info("Successfully created time slot: {}", timeSlotId);
                        return timeSlotId.toString();
                    }
                }
            }
            logger.warn("Could not parse time slot creation response");
            return null;

        } catch (Exception e) {
            logger.error("Failed to create time slot. DoctorId: {}. Error: {}", doctorId, e.getMessage());
            return null;
        }
    }
}
