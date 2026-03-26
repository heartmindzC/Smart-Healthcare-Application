package com.example.appointmentservice.command;

import com.example.appointmentservice.model.Appointment;;

public interface AppointmentCommand {
    Appointment execute();
}
