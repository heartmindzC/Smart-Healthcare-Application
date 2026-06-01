package com.example.appointmentservice.mapper;

import com.example.appointmentservice.dto.reuqest.AppointmentCreateRequest;
import com.example.appointmentservice.dto.reuqest.AppointmentRequest;
import com.example.appointmentservice.model.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    Appointment toAppointment(AppointmentCreateRequest appointmentRequest);
    void update(@MappingTarget Appointment appointment, AppointmentRequest request);
}
