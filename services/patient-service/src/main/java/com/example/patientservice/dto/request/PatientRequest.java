package com.example.patientservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    @NotNull
    private String userId;
    @NotNull
    private String fullName;
    @NotNull
    private String birth;
    @NotNull
    private String gender;
    @NotNull
    private String insuranceId;
    @NotNull
    private String emergencyCallingNumber;
    @NotNull
    private String job;
    @NotNull
    private String bloodType;
    @NotNull
    private Double heights;
    @NotNull
    private Double weights;
}
