package com.example.doctorservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest {
    @NotEmpty(message = "HOSPITAL_ID_NULL")
    private String hospitalId;
    @NotEmpty(message = "DEPARTMENT_NULL")
    private String department;
}
