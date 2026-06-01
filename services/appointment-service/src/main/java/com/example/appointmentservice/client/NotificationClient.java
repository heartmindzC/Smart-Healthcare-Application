package com.example.appointmentservice.client;

import com.example.appointmentservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Client để gọi Notification Service.
 * Dùng để gửi email notification cho user và doctor khi có thay đổi appointment.
 * Là implementation cơ bản (base) cho decorator pattern.
 */
// @Component
public class NotificationClient implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationClient.class);

    private final String notificationServiceUrl;
    private final RestTemplate restTemplate;

    public NotificationClient(@Value("${services.notification.url:http://notification-service:8088}") String notificationServiceUrl) {
        this.notificationServiceUrl = notificationServiceUrl;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void sendAppointmentConfirmationToPatient(Map<String, Object> notificationData) {
        String url = notificationServiceUrl + "/notifications/appointment/patient";
        logger.info("[NotificationClient] Sending appointment confirmation to patient via: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

        restTemplate.postForEntity(url, request, String.class);
        logger.info("[NotificationClient] Successfully sent appointment confirmation to patient");
    }

    @Override
    public void sendAppointmentNotificationToDoctor(Map<String, Object> notificationData) {
        String url = notificationServiceUrl + "/notifications/appointment/doctor";
        logger.info("[NotificationClient] Sending appointment notification to doctor via: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

        restTemplate.postForEntity(url, request, String.class);
        logger.info("[NotificationClient] Successfully sent appointment notification to doctor");
    }

    @Override
    public void sendAppointmentCancellationToPatient(Map<String, Object> notificationData) {
        String url = notificationServiceUrl + "/notifications/appointment/cancel/patient";
        logger.info("[NotificationClient] Sending cancellation notification to patient via: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

        restTemplate.postForEntity(url, request, String.class);
        logger.info("[NotificationClient] Successfully sent cancellation notification to patient");
    }

    @Override
    public void sendAppointmentCancellationToDoctor(Map<String, Object> notificationData) {
        String url = notificationServiceUrl + "/notifications/appointment/cancel/doctor";
        logger.info("[NotificationClient] Sending cancellation notification to doctor via: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

        restTemplate.postForEntity(url, request, String.class);
        logger.info("[NotificationClient] Successfully sent cancellation notification to doctor");
    }

    @Override
    public void sendAppointmentCompletionToPatient(Map<String, Object> notificationData) {
        String url = notificationServiceUrl + "/notifications/appointment/complete/patient";
        logger.info("[NotificationClient] Sending completion notification to patient via: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

        restTemplate.postForEntity(url, request, String.class);
        logger.info("[NotificationClient] Successfully sent completion notification to patient");
    }

    @Override
    public void sendAppointmentCompletionToDoctor(Map<String, Object> notificationData) {
        String url = notificationServiceUrl + "/notifications/appointment/complete/doctor";
        logger.info("[NotificationClient] Sending completion notification to doctor via: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(notificationData, headers);

        restTemplate.postForEntity(url, request, String.class);
        logger.info("[NotificationClient] Successfully sent completion notification to doctor");
    }
}