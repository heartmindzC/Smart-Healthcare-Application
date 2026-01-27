package com.example.hospitalservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateRequest {
    @NotNull
    private String departmentName;
    @NotNull
    private String departmentPhone;
    @NotNull
    private String departmentEmail;
    @NotNull
    private String hospitalId;
}


