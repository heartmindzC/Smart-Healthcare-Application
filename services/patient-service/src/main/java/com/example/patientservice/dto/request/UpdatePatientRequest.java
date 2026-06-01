package com.example.patientservice.dto.request;

import com.example.patientservice.model.BloodType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {
    @NotNull
    private String insuranceId;
    @NotNull
    private String emergencyCallingNumber;
    @NotNull
    private BloodType bloodType;
    @NotNull
    private Double heights;
    @NotNull
    private Double weights;
}

