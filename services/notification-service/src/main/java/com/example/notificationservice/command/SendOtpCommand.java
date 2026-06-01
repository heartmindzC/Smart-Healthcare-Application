package com.example.notificationservice.command;

import com.example.notificationservice.dto.OtpNotificationRequest;
import com.example.notificationservice.service.NotificationService;

import java.util.HashMap;
import java.util.Map;

public class SendOtpCommand implements NotificationCommand {

    private final NotificationService notificationService;
    private final OtpNotificationRequest request;

    public SendOtpCommand(NotificationService notificationService, OtpNotificationRequest request) {
        this.notificationService = notificationService;
        this.request = request;
    }

    @Override
    public void execute() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", request.getFullName());
        variables.put("otp", request.getOtp());

        notificationService.sendHtmlEmail(
                request.getEmail(),
                "Ma xac thuc (OTP) dat lai mat khau - Smart Healthcare",
                "otp-email",
                variables
        );
    }
}