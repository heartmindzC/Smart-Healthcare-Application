package com.example.hospitalservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalCreateRequest {
    @NotNull
    private String hospitalName;
    @NotNull
    private String hospitalAddress;
    @NotNull
    private String hospitalPhone;
    @NotNull
    private String hospitalEmail;
}


