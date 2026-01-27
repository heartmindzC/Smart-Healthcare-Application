package com.example.hospitalservice.mapper;

import com.example.hospitalservice.dto.request.DepartmentCreateRequest;
import com.example.hospitalservice.dto.request.DepartmentRequest;
import com.example.hospitalservice.model.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department toDepartment(DepartmentCreateRequest request);
}
