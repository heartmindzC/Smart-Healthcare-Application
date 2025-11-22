package com.example.hospitalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalCreateRequest {
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalPhone;
    private String hospitalEmail;
}

