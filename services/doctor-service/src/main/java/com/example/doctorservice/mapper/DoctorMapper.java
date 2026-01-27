package com.example.doctorservice.mapper;

import com.example.doctorservice.dto.request.DoctorCreationRequest;
import com.example.doctorservice.model.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    Doctor toDoctor(DoctorCreationRequest request);
}
