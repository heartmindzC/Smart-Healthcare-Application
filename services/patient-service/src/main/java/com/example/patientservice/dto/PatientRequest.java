package com.example.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    private String userId;
    private String fullName;
    private String birth; // Format: "yyyy-MM-dd"
    private String gender; // "MALE" hoặc "FEMALE"
    private String insuranceId;
    private String emergencyCallingNumber;
    private String job;
    private String bloodType; // "A", "B", "AB", "O"
    private Double heights;
    private Double weights;
}
