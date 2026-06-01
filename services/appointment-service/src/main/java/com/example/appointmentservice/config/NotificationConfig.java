package com.example.appointmentservice.config;

import com.example.appointmentservice.client.NotificationClient;
import com.example.appointmentservice.decorator.LoggingNotificationDecorator;
import com.example.appointmentservice.decorator.RetryNotificationDecorator;
import com.example.appointmentservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình notification service với decorator pattern.
 * Wrap order: NotificationClient (base) → LoggingDecorator → RetryDecorator
 * 
 * Listener sẽ inject NotificationService (chính là RetryDecorator sau khi wrap).
 */
@Configuration
public class NotificationConfig {

    @Value("${notification.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${notification.retry.delay-ms:1000}")
    private long delayMs;

    @Bean
    public NotificationService notificationService(
            @Value("${services.notification.url:http://notification-service:8088}") String notificationUrl) {
        NotificationClient baseClient = new NotificationClient(notificationUrl);
        return new RetryNotificationDecorator(
            new LoggingNotificationDecorator(baseClient),
            maxAttempts,
            delayMs
        );
    }
}