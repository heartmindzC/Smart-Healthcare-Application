package com.example.notificationservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentNotificationRequest {
    private String doctorEmail;
    private String patientName;
    private String doctorName;
    private String hospitalName;
    private String departmentName;
    private String reason;
    private String appointmentDateTime;
}
