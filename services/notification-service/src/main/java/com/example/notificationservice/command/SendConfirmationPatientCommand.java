package com.example.notificationservice.command;

import com.example.notificationservice.client.PatientServiceClient;
import com.example.notificationservice.client.UserServiceClient;
import com.example.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SendConfirmationPatientCommand implements NotificationCommand {

    private final NotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final Map<String, Object> notificationData;
    private final PatientServiceClient patientServiceClient;

    public SendConfirmationPatientCommand(NotificationService notificationService,
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
        variables.put("title", "Xác Nhận Đặt Lịch Hẹn");
        variables.put("patientName", notificationData.get("patientName"));
        variables.put("message", "Lịch hẹn khám bệnh của bạn đã được xác nhận thành công. Vui lòng đến trước giờ hẹn 15 phút.");
        variables.put("appointmentId", notificationData.get("appointmentId"));
        variables.put("doctorName", notificationData.get("doctorName"));
        variables.put("hospitalName", notificationData.get("hospitalName"));
        variables.put("departmentName", notificationData.get("departmentName"));
        variables.put("reason", notificationData.get("reason"));
        variables.put("appointmentTime", notificationData.get("appointmentDateTime"));

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
                    "Xác nhận lịch hẹn khám bệnh - Smart Healthcare",
                    "appointment-email",
                    variables
            );
            log.info("[SendConfirmationPatientCommand] Sent confirmation email to patient: {}", patientEmail);
        } else {
            log.warn("[SendConfirmationPatientCommand] Patient email not found for patientId: {}", notificationData.get("patientId"));
        }
    }
}
