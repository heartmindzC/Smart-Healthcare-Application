package com.example.patientservice.mapper;

import com.example.patientservice.dto.request.PatientRequest;
import com.example.patientservice.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "birth", ignore = true)
    Patient toPatient(PatientRequest patient);
}
