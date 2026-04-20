package com.example.doctorservice.dto;

import com.example.doctorservice.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private boolean status;
    private String message;
    private List<Doctor> result;
}
