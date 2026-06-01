package com.example.appointmentservice.decorator;

import com.example.appointmentservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Decorator thêm logging cho notification service.
 * Ghi log thời gian bắt đầu, kết thúc và duration của mỗi lần gọi.
 * 
 * NOTE: Không có @Component - được tạo bởi NotificationConfig
 */
@Slf4j
public class LoggingNotificationDecorator implements NotificationService {

    private final NotificationService wrapped;

    public LoggingNotificationDecorator(NotificationService wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void sendAppointmentConfirmationToPatient(Map<String, Object> notificationData) {
        String appointmentId = String.valueOf(notificationData.getOrDefault("appointmentId", "unknown"));
        long startTime = System.currentTimeMillis();

        log.info(">>> [START] Sending confirmation to patient | appointmentId={}", appointmentId);
        try {
            wrapped.sendAppointmentConfirmationToPatient(notificationData);
            long duration = System.currentTimeMillis() - startTime;
            log.info("<<< [SUCCESS] Confirmation sent to patient | appointmentId={} | duration={}ms",
                    appointmentId, duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("<<< [FAIL] Confirmation failed to patient | appointmentId={} | error={} | duration={}ms",
                    appointmentId, e.getMessage(), duration);
            throw e;
        }
    }

    @Override
    public void sendAppointmentNotificationToDoctor(Map<String, Object> notificationData) {
        String appointmentId = String.valueOf(notificationData.getOrDefault("appointmentId", "unknown"));
        long startTime = System.currentTimeMillis();

        log.info(">>> [START] Sending notification to doctor | appointmentId={}", appointmentId);
        try {
            wrapped.sendAppointmentNotificationToDoctor(notificationData);
            long duration = System.currentTimeMillis() - startTime;
            log.info("<<< [SUCCESS] Notification sent to doctor | appointmentId={} | duration={}ms",
                    appointmentId, duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("<<< [FAIL] Notification failed to doctor | appointmentId={} | error={} | duration={}ms",
                    appointmentId, e.getMessage(), duration);
            throw e;
        }
    }

    @Override
    public void sendAppointmentCancellationToPatient(Map<String, Object> notificationData) {
        String appointmentId = String.valueOf(notificationData.getOrDefault("appointmentId", "unknown"));
        long startTime = System.currentTimeMillis();

        log.info(">>> [START] Sending cancellation to patient | appointmentId={}", appointmentId);
        try {
            wrapped.sendAppointmentCancellationToPatient(notificationData);
            long duration = System.currentTimeMillis() - startTime;
            log.info("<<< [SUCCESS] Cancellation sent to patient | appointmentId={} | duration={}ms",
                    appointmentId, duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("<<< [FAIL] Cancellation failed to patient | appointmentId={} | error={} | duration={}ms",
                    appointmentId, e.getMessage(), duration);
            throw e;
        }
    }

    @Override
    public void sendAppointmentCancellationToDoctor(Map<String, Object> notificationData) {
        String appointmentId = String.valueOf(notificationData.getOrDefault("appointmentId", "unknown"));
        long startTime = System.currentTimeMillis();

        log.info(">>> [START] Sending cancellation to doctor | appointmentId={}", appointmentId);
        try {
            wrapped.sendAppointmentCancellationToDoctor(notificationData);
            long duration = System.currentTimeMillis() - startTime;
            log.info("<<< [SUCCESS] Cancellation sent to doctor | appointmentId={} | duration={}ms",
                    appointmentId, duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("<<< [FAIL] Cancellation failed to doctor | appointmentId={} | error={} | duration={}ms",
                    appointmentId, e.getMessage(), duration);
            throw e;
        }
    }

    @Override
    public void sendAppointmentCompletionToPatient(Map<String, Object> notificationData) {
        String appointmentId = String.valueOf(notificationData.getOrDefault("appointmentId", "unknown"));
        long startTime = System.currentTimeMillis();

        log.info(">>> [START] Sending completion to patient | appointmentId={}", appointmentId);
        try {
            wrapped.sendAppointmentCompletionToPatient(notificationData);
            long duration = System.currentTimeMillis() - startTime;
            log.info("<<< [SUCCESS] Completion sent to patient | appointmentId={} | duration={}ms",
                    appointmentId, duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("<<< [FAIL] Completion failed to patient | appointmentId={} | error={} | duration={}ms",
                    appointmentId, e.getMessage(), duration);
            throw e;
        }
    }

    @Override
    public void sendAppointmentCompletionToDoctor(Map<String, Object> notificationData) {
        String appointmentId = String.valueOf(notificationData.getOrDefault("appointmentId", "unknown"));
        long startTime = System.currentTimeMillis();

        log.info(">>> [START] Sending completion to doctor | appointmentId={}", appointmentId);
        try {
            wrapped.sendAppointmentCompletionToDoctor(notificationData);
            long duration = System.currentTimeMillis() - startTime;
            log.info("<<< [SUCCESS] Completion sent to doctor | appointmentId={} | duration={}ms",
                    appointmentId, duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("<<< [FAIL] Completion failed to doctor | appointmentId={} | error={} | duration={}ms",
                    appointmentId, e.getMessage(), duration);
            throw e;
        }
    }
}