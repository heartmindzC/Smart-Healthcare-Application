package com.example.patientservice.dto;

import com.example.patientservice.model.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private boolean status;
    private String message;
    private Patient result;
}

