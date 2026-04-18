package com.example.notificationservice.command;

import com.example.notificationservice.dto.AppointmentNotificationRequest;
import com.example.notificationservice.service.NotificationService;

import java.util.HashMap;
import java.util.Map;

public class SendAppointmentDoctorCommand implements NotificationCommand {

    private final NotificationService notificationService;
    private final AppointmentNotificationRequest request;

    public SendAppointmentDoctorCommand(NotificationService notificationService, AppointmentNotificationRequest request) {
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
        
        variables.put("title", "Lịch Khám Bệnh Mới");
        variables.put("patientName", "Bác sĩ " + request.getDoctorName());
        variables.put("message", "Hệ thống vừa ghi nhận một lịch hẹn khám bệnh mới dành cho bác sĩ.");

        notificationService.sendHtmlEmail(
                request.getDoctorEmail(),
                "Bạn có lịch hẹn khám mới - Smart Healthcare",
                "appointment-email",
                variables
        );
    }
}