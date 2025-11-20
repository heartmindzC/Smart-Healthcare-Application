package com.example.ehrservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private String doctorId;
    private String fullName;
    private String specialization;
    private String hospital;
    private String department;
}


