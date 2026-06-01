package com.example.appointmentservice.decorator;

import com.example.appointmentservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * Decorator thêm retry logic cho notification service.
 * Thử lại request khi gọi notification service thất bại.
 * 
 * NOTE: Không có @Component - được tạo bởi NotificationConfig
 */
@Slf4j
public class RetryNotificationDecorator implements NotificationService {

    private final NotificationService wrapped;
    private final int maxAttempts;
    private final long delayMs;

    public RetryNotificationDecorator(NotificationService wrapped, 
                                       @Value("${notification.retry.max-attempts:3}") int maxAttempts,
                                       @Value("${notification.retry.delay-ms:1000}") long delayMs) {
        this.wrapped = wrapped;
        this.maxAttempts = maxAttempts;
        this.delayMs = delayMs;
    }

    @Override
    public void sendAppointmentConfirmationToPatient(Map<String, Object> notificationData) {
        executeWithRetry("sendAppointmentConfirmationToPatient", notificationData, 
                data -> wrapped.sendAppointmentConfirmationToPatient(data));
    }

    @Override
    public void sendAppointmentNotificationToDoctor(Map<String, Object> notificationData) {
        executeWithRetry("sendAppointmentNotificationToDoctor", notificationData, 
                data -> wrapped.sendAppointmentNotificationToDoctor(data));
    }

    @Override
    public void sendAppointmentCancellationToPatient(Map<String, Object> notificationData) {
        executeWithRetry("sendAppointmentCancellationToPatient", notificationData, 
                data -> wrapped.sendAppointmentCancellationToPatient(data));
    }

    @Override
    public void sendAppointmentCancellationToDoctor(Map<String, Object> notificationData) {
        executeWithRetry("sendAppointmentCancellationToDoctor", notificationData, 
                data -> wrapped.sendAppointmentCancellationToDoctor(data));
    }

    @Override
    public void sendAppointmentCompletionToPatient(Map<String, Object> notificationData) {
        executeWithRetry("sendAppointmentCompletionToPatient", notificationData, 
                data -> wrapped.sendAppointmentCompletionToPatient(data));
    }

    @Override
    public void sendAppointmentCompletionToDoctor(Map<String, Object> notificationData) {
        executeWithRetry("sendAppointmentCompletionToDoctor", notificationData, 
                data -> wrapped.sendAppointmentCompletionToDoctor(data));
    }

    private void executeWithRetry(String methodName, Map<String, Object> data, NotificationOperation operation) {
        String appointmentId = String.valueOf(data.getOrDefault("appointmentId", "unknown"));
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                operation.execute(data);
                if (attempt > 1) {
                    log.info("[RETRY] Success on attempt {}/{} | method={} | appointmentId={}",
                            attempt, maxAttempts, methodName, appointmentId);
                }
                return;
            } catch (Exception e) {
                lastException = e;
                log.warn("[RETRY] Attempt {}/{} failed | method={} | appointmentId={} | error={}",
                        attempt, maxAttempts, methodName, appointmentId, e.getMessage());

                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry interrupted", ie);
                    }
                }
            }
        }

        log.error("[RETRY] All {} attempts failed | method={} | appointmentId={}",
                maxAttempts, methodName, appointmentId);
        throw new RuntimeException("Notification failed after " + maxAttempts + " attempts: " + methodName, lastException);
    }

    @FunctionalInterface
    private interface NotificationOperation {
        void execute(Map<String, Object> data);
    }
}