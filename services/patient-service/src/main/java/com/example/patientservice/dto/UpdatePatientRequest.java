package com.example.patientservice.dto;

import com.example.patientservice.model.BloodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {
    private String insuranceId;
    private String emergencyCallingNumber;
    private String job;
    private BloodType bloodType;
    private Double heights;
    private Double weights;
}

