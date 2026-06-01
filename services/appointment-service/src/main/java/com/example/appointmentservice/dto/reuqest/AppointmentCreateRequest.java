package com.example.appointmentservice.dto.reuqest;

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
public class AppointmentCreateRequest {
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
    

    @NotNull(message = "APPOINTMENT_DATE_TIME_NULL")
    @Future(message = "APPOINTMENT_DATE_TIME_INVALID")
    private LocalDateTime appointmentDateTime;
    
    private String notes;
    
    private String reason;
}

