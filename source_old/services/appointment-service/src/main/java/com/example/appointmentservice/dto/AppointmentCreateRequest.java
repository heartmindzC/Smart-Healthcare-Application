package com.example.appointmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateRequest {
    private int doctorId;
    private String doctorName;
    private int patientId;
    private String patientName;
    private int hospitalId;
    private String hospitalName;
    private Integer departmentId;
    private String departmentName;
    private Integer timeSlotId;
    private LocalDateTime appointmentDateTime;
    private String notes;
    private String reason;
}

