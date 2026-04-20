package com.example.hospitalservice.dto;

import com.example.hospitalservice.model.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private boolean status;
    private String message;
    private List<Department> result;
}


