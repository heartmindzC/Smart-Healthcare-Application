package com.example.appointmentservice.dto;

import com.example.appointmentservice.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private boolean status;
    private String message;
    private List<Appointment> result;
}

