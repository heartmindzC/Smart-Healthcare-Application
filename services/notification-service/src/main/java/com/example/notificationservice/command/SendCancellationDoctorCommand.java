package com.example.notificationservice.command;

import com.example.notificationservice.client.UserServiceClient;
import com.example.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SendCancellationDoctorCommand implements NotificationCommand {

    private final NotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final Map<String, Object> notificationData;

    public SendCancellationDoctorCommand(NotificationService notificationService,
                                          UserServiceClient userServiceClient,
                                          Map<String, Object> notificationData) {
        this.notificationService = notificationService;
        this.userServiceClient = userServiceClient;
        this.notificationData = notificationData;
    }

    @Override
    public void execute() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("title", "Lịch Hẹn Đã Bị Hủy");
        variables.put("patientName", notificationData.get("patientName"));
        variables.put("message", "Một lịch hẹn khám bệnh đã bị hủy. Timeslot đã được giải phóng.");
        variables.put("appointmentId", notificationData.get("appointmentId"));
        variables.put("doctorName", notificationData.get("doctorName"));
        variables.put("hospitalName", notificationData.get("hospitalName"));
        variables.put("departmentName", notificationData.get("departmentName"));
        variables.put("reason", notificationData.get("reason"));

        String doctorEmail = (String) notificationData.get("doctorEmail");
        if (doctorEmail == null || doctorEmail.isBlank()) {
            String doctorId = (String) notificationData.get("doctorId");
            doctorEmail = userServiceClient.getUserEmail(doctorId).orElse(null);
        }

        if (doctorEmail != null && !doctorEmail.isBlank()) {
            notificationService.sendHtmlEmail(
                    doctorEmail,
                    "Thông báo hủy lịch hẹn khám bệnh - Smart Healthcare",
                    "appointment-cancel-email",
                    variables
            );
            log.info("[SendCancellationDoctorCommand] Sent cancellation email to doctor: {}", doctorEmail);
        } else {
            log.warn("[SendCancellationDoctorCommand] Doctor email not found for doctorId: {}", notificationData.get("doctorId"));
        }
    }
}
