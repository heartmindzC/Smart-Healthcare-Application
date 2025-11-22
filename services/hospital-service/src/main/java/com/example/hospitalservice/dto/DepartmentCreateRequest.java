package com.example.hospitalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentCreateRequest {
    private String departmentName;
    private String departmentPhone;
    private String departmentEmail;
    private int hospitalId;
}

