package com.example.hospitalservice.mapper;

import com.example.hospitalservice.dto.request.HospitalCreateRequest;
import com.example.hospitalservice.model.Hospital;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HospitalMapper {
    Hospital toHospital(HospitalCreateRequest hospital);
}
