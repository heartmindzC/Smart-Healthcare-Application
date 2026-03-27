package com.example.notificationservice.command;

import com.example.notificationservice.dto.UserRegistrationRequest;
import com.example.notificationservice.service.NotificationService;

import java.util.HashMap;
import java.util.Map;

public class SendEmailCommand implements NotificationCommand {

    private final NotificationService notificationService;
    private final UserRegistrationRequest request;

    public SendEmailCommand(NotificationService notificationService, UserRegistrationRequest request) {
        this.notificationService = notificationService;
        this.request = request;
    }

    @Override
    public void execute() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", request.getFullName());
        variables.put("userId", request.getUserId());
        variables.put("email", request.getEmail());
        variables.put("phone", request.getPhone());

        notificationService.sendHtmlEmail(
                request.getEmail(),
                "He thong Smart Healthcare",
                "welcome-email",
                variables
        );
    }
}