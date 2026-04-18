package com.example.notificationservice.command;

import com.example.notificationservice.client.UserServiceClient;
import com.example.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SendCompletionDoctorCommand implements NotificationCommand {

    private final NotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final Map<String, Object> notificationData;

    public SendCompletionDoctorCommand(NotificationService notificationService,
                                        UserServiceClient userServiceClient,
                                        Map<String, Object> notificationData) {
        this.notificationService = notificationService;
        this.userServiceClient = userServiceClient;
        this.notificationData = notificationData;
    }

    @Override
    public void execute() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("title", "Lịch Hẹn Đã Hoàn Thành");
        variables.put("patientName", notificationData.get("patientName"));
        variables.put("message", "Lịch hẹn khám bệnh đã được hoàn thành. Cảm ơn bác sĩ đã khám bệnh.");
        variables.put("appointmentId", notificationData.get("appointmentId"));
        variables.put("doctorName", notificationData.get("doctorName"));
        variables.put("hospitalName", notificationData.get("hospitalName"));
        variables.put("departmentName", notificationData.get("departmentName"));

        String doctorEmail = (String) notificationData.get("doctorEmail");
        if (doctorEmail == null || doctorEmail.isBlank()) {
            String doctorId = (String) notificationData.get("doctorId");
            doctorEmail = userServiceClient.getUserEmail(doctorId).orElse(null);
        }

        if (doctorEmail != null && !doctorEmail.isBlank()) {
            notificationService.sendHtmlEmail(
                    doctorEmail,
                    "Lịch hẹn khám bệnh đã hoàn thành - Smart Healthcare",
                    "appointment-complete-email",
                    variables
            );
            log.info("[SendCompletionDoctorCommand] Sent completion email to doctor: {}", doctorEmail);
        } else {
            log.warn("[SendCompletionDoctorCommand] Doctor email not found for doctorId: {}", notificationData.get("doctorId"));
        }
    }
}
