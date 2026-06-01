package com.example.appointmentservice.dto.reuqest;

import com.example.appointmentservice.model.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    @NotEmpty(message = "DOCTOR_ID_NULL")
    private String doctorId;
    
    @NotEmpty(message = "DOCTOR_NAME_NULL")
    private String doctorName;
    
    @NotEmpty(message = "PATIENT_ID_NULL")
    private String patientId;
    
    @NotEmpty(message = "PATIENT_NAME_NULL")
    private String patientName;
    
    @NotEmpty(message = "HOSPITAL_ID_NULL")
    private String hospitalId;
    
    @NotEmpty(message = "HOSPITAL_NAME_NULL")
    private String hospitalName;
    
    @NotEmpty(message = "DEPARTMENT_ID_NULL")
    private String departmentId;
    
    @NotEmpty(message = "DEPARTMENT_NAME_NULL")
    private String departmentName;
    
    @NotEmpty(message = "TIME_SLOT_ID_NULL")
    private String timeSlotId;
    
    @NotNull(message = "APPOINTMENT_DATE_TIME_NULL")
    private LocalDateTime appointmentDateTime;
    
    @NotNull(message = "STATUS_NULL")
    private AppointmentStatus status;
    
    private String notes;
    
    private String reason;
}

