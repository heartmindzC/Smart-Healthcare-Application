package com.example.appointmentservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Client để gọi Notification Service.
 * Dùng để gửi email notification cho user và doctor khi có thay đổi appointment.
 */
@Component
public class NotificationClient {

    private static final Logger logger = LoggerFactory.getLogger(NotificationClient.class);

    @Value("${services.notification.url:http://notification-service:8088}")
    private String notificationServiceUrl;

    private final RestTemplate restTemplate;

    public NotificationClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Gửi email xác nhận lịch hẹn cho bệnh nhân.
     */
    public void sendAppointmentConfirmationToPatient(Map<String, Object> notificationData) {
        try {
            String url = notificationServiceUrl + "/notifications/appointment/patient";
            logger.info("[NotificationClient] Sending appointment confirmation to patient via: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

            restTemplate.postForEntity(url, request, String.class);
            logger.info("[NotificationClient] Successfully sent appointment confirmation to patient");

        } catch (Exception e) {
            logger.error("[NotificationClient] Failed to send appointment confirmation to patient: {}", e.getMessage());
        }
    }

    /**
     * Gửi email thông báo lịch hẹn mới cho bác sĩ.
     */
    public void sendAppointmentNotificationToDoctor(Map<String, Object> notificationData) {
        try {
            String url = notificationServiceUrl + "/notifications/appointment/doctor";
            logger.info("[NotificationClient] Sending appointment notification to doctor via: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

            restTemplate.postForEntity(url, request, String.class);
            logger.info("[NotificationClient] Successfully sent appointment notification to doctor");

        } catch (Exception e) {
            logger.error("[NotificationClient] Failed to send appointment notification to doctor: {}", e.getMessage());
        }
    }

    /**
     * Gửi email thông báo hủy lịch hẹn cho bệnh nhân.
     */
    public void sendAppointmentCancellationToPatient(Map<String, Object> notificationData) {
        try {
            String url = notificationServiceUrl + "/notifications/appointment/cancel/patient";
            logger.info("[NotificationClient] Sending cancellation notification to patient via: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

            restTemplate.postForEntity(url, request, String.class);
            logger.info("[NotificationClient] Successfully sent cancellation notification to patient");

        } catch (Exception e) {
            logger.error("[NotificationClient] Failed to send cancellation notification to patient: {}", e.getMessage());
        }
    }

    /**
     * Gửi email thông báo hủy lịch hẹn cho bác sĩ.
     */
    public void sendAppointmentCancellationToDoctor(Map<String, Object> notificationData) {
        try {
            String url = notificationServiceUrl + "/notifications/appointment/cancel/doctor";
            logger.info("[NotificationClient] Sending cancellation notification to doctor via: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

            restTemplate.postForEntity(url, request, String.class);
            logger.info("[NotificationClient] Successfully sent cancellation notification to doctor");

        } catch (Exception e) {
            logger.error("[NotificationClient] Failed to send cancellation notification to doctor: {}", e.getMessage());
        }
    }

    /**
     * Gửi email thông báo hoàn thành lịch hẹn cho bệnh nhân.
     */
    public void sendAppointmentCompletionToPatient(Map<String, Object> notificationData) {
        try {
            String url = notificationServiceUrl + "/notifications/appointment/complete/patient";
            logger.info("[NotificationClient] Sending completion notification to patient via: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

            restTemplate.postForEntity(url, request, String.class);
            logger.info("[NotificationClient] Successfully sent completion notification to patient");

        } catch (Exception e) {
            logger.error("[NotificationClient] Failed to send completion notification to patient: {}", e.getMessage());
        }
    }

    /**
     * Gửi email thông báo hoàn thành lịch hẹn cho bác sĩ.
     */
    public void sendAppointmentCompletionToDoctor(Map<String, Object> notificationData) {
        try {
            String url = notificationServiceUrl + "/notifications/appointment/complete/doctor";
            logger.info("[NotificationClient] Sending completion notification to doctor via: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

            restTemplate.postForEntity(url, request, String.class);
            logger.info("[NotificationClient] Successfully sent completion notification to doctor");

        } catch (Exception e) {
            logger.error("[NotificationClient] Failed to send completion notification to doctor: {}", e.getMessage());
        }
    }
}
