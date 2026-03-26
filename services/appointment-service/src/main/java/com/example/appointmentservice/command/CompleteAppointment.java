package com.example.appointmentservice.command;

import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.service.AppointmentService;

public class CompleteAppointment implements AppointmentCommand{
    private final AppointmentService appointmentService;
    private final String appointmentId;

    public CompleteAppointment(AppointmentService appointmentService, String appointmentId){
        this.appointmentService = appointmentService;
        this.appointmentId = appointmentId;
    }

    @Override
    public Appointment execute(){
        //Logic cho Complete...
        return appointmentService.performStatusUpdate(appointmentId, AppointmentStatus.COMPLETED);
    }
}
