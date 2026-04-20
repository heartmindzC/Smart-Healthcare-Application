package com.example.doctorservice.dto;

import com.example.doctorservice.model.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotResponse {
    private boolean status;
    private String message;
    private List<TimeSlot> result;
}

