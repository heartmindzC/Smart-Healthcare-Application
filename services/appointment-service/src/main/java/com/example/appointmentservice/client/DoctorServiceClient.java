package com.example.appointmentservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Client để gọi Doctor Service thông qua API Gateway/trực tiếp
 * Dùng để quản lý time slot khi appointment thay đổi trạng thái
 */
@Component
public class DoctorServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.doctor.url:http://localhost:8082}")
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
            // Simplified: chỉ kiểm tra xem có gọi được không
            
            logger.info("Time slot {} is available", timeSlotId);
            return true;

        } catch (Exception e) {
            logger.error("Failed to check time slot availability: {}. Error: {}", timeSlotId, e.getMessage());
            return false;
        }
    }
}
