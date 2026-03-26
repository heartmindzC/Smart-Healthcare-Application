package com.example.appointmentservice.command;

import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.service.AppointmentService;

public class ConfirmAppointment implements AppointmentCommand {
    private final AppointmentService appointmentService;
    private final String appointmentId;

    public ConfirmAppointment(AppointmentService appointmentService, String appointmentId){
        this.appointmentService = appointmentService;
        this.appointmentId = appointmentId;
    }

    @Override
    public Appointment execute(){
        return appointmentService.performStatusUpdate(appointmentId, AppointmentStatus.CONFIRMED);
    }
}