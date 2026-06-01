package com.example.notificationservice.command;

import com.example.notificationservice.client.PatientServiceClient;
import com.example.notificationservice.client.UserServiceClient;
import com.example.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SendCompletionPatientCommand implements NotificationCommand {

    private final NotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final Map<String, Object> notificationData;
    private final PatientServiceClient patientServiceClient;

    public SendCompletionPatientCommand(NotificationService notificationService,
                                         UserServiceClient userServiceClient,
                                         Map<String, Object> notificationData,
                                        PatientServiceClient patientServiceClient) {
        this.notificationService = notificationService;
        this.userServiceClient = userServiceClient;
        this.notificationData = notificationData;
        this.patientServiceClient = patientServiceClient;
    }

    @Override
    public void execute() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("title", "Lịch Hẹn Đã Hoàn Thành");
        variables.put("patientName", notificationData.get("patientName"));
        variables.put("message", "Cảm ơn bạn đã sử dụng dịch vụ. Lịch hẹn khám bệnh của bạn đã hoàn thành.");
        variables.put("appointmentId", notificationData.get("appointmentId"));
        variables.put("doctorName", notificationData.get("doctorName"));
        variables.put("hospitalName", notificationData.get("hospitalName"));
        variables.put("departmentName", notificationData.get("departmentName"));

        String patientEmail = (String) notificationData.get("patientEmail");
        if (patientEmail == null || patientEmail.isBlank()) {
            String patientId = (String) notificationData.get("patientId");
            String userId = patientServiceClient.getUserIdByPatientId(patientId);
            if (userId.isEmpty()) {
                throw new RuntimeException("User not found");
            }
            patientEmail = userServiceClient.getUserEmail(userId).orElse(null);
        }

        if (patientEmail != null && !patientEmail.isBlank()) {
            notificationService.sendHtmlEmail(
                    patientEmail,
                    "Lịch hẹn khám bệnh đã hoàn thành - Smart Healthcare",
                    "appointment-complete-email",
                    variables
            );
            log.info("[SendCompletionPatientCommand] Sent completion email to patient: {}", patientEmail);
        } else {
            log.warn("[SendCompletionPatientCommand] Patient email not found for patientId: {}", notificationData.get("patientId"));
        }
    }
}
