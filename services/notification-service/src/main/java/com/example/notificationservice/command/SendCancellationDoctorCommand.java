package com.example.notificationservice.command;

import com.example.notificationservice.client.DoctorServiceClient;
import com.example.notificationservice.client.UserServiceClient;
import com.example.notificationservice.service.NotificationService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SendCancellationDoctorCommand implements NotificationCommand {

    private final NotificationService notificationService;
    private final UserServiceClient userServiceClient;
    private final Map<String, Object> notificationData;
    private final DoctorServiceClient doctorServiceClient;

    public SendCancellationDoctorCommand(NotificationService notificationService,
                                          UserServiceClient userServiceClient,
                                          Map<String, Object> notificationData,
                                          DoctorServiceClient doctorServiceClient) {
        this.notificationService = notificationService;
        this.userServiceClient = userServiceClient;
        this.notificationData = notificationData;
        this.doctorServiceClient = doctorServiceClient;
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
            String userId = doctorServiceClient.getUserIdByDoctorId(doctorId);
            if (userId.isEmpty()) {
                throw new RuntimeException("User not found");
            }
            doctorEmail = userServiceClient.getUserEmail(userId).orElse(null);
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
