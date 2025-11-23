package com.example.appointmentservice.dto;

import com.example.appointmentservice.model.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private Integer appointmentId;
    private Integer doctorId;
    private String doctorName;
    private Integer patientId;
    private String patientName;
    private Integer hospitalId;
    private String hospitalName;
    private Integer departmentId;
    private String departmentName;
    private Integer timeSlotId;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String notes;
    private String reason;
}

