package com.example.doctorservice.mapper;

import com.example.doctorservice.dto.request.TimeSlotRequest;
import com.example.doctorservice.model.TimeSlot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper {
    TimeSlot toTimeSlot(TimeSlotRequest request);
}
