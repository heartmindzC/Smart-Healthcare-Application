package com.example.ehrservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Integer patientId;
    private String fullName;
    private String userId;
    private Date birth;
    private Date registeredAt;
    private String gender;
    private String insuranceId;
    private String emergencyCallingNumber;
    private String job;
    private String bloodType;
    private Double heights;
    private Double weights;
    private Boolean isActive;
}


