package com.example.notificationservice.command;

import com.example.notificationservice.dto.AppointmentNotificationRequest;
import com.example.notificationservice.service.NotificationService;

import java.util.HashMap;
import java.util.Map;

public class SendAppointmentPatientCommand implements NotificationCommand {

    private final NotificationService notificationService;
    private final AppointmentNotificationRequest request;

    public SendAppointmentPatientCommand(NotificationService notificationService,
            AppointmentNotificationRequest request) {
        this.notificationService = notificationService;
        this.request = request;
    }

    @Override
    public void execute() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("patientName", request.getPatientName());
        variables.put("doctorName", request.getDoctorName());
        variables.put("appointmentTime", request.getAppointmentDateTime());
        variables.put("hospitalName", request.getHospitalName());
        variables.put("departmentName", request.getDepartmentName());
        variables.put("reason", request.getReason());

        variables.put("title", "Xác Nhận Đặt Lịch Hẹn");
        variables.put("patientName", request.getPatientName());
        variables.put("message",
                "Lịch hẹn khám bệnh của bạn đã được hệ thống xác nhận thành công. Vui lòng đến trước giờ hẹn 15 phút.");

        notificationService.sendHtmlEmail(
                request.getPatientEmail(),
                "Xác nhận lịch hẹn khám bệnh - Smart Healthcare",
                "appointment-email",
                variables);
    }
}
