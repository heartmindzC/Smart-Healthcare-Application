package com.example.appointmentservice.command;

import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.service.AppointmentService;

public class CancelAppointment implements AppointmentCommand {
    private final AppointmentService appointmentService;
    private final String appointmentId;

    public CancelAppointment(AppointmentService appointmentService, String appointmentId){
        this.appointmentService = appointmentService;
        this.appointmentId = appointmentId;
    }

    @Override
    public Appointment execute(){
        Appointment updateAppointment = appointmentService.performStatusUpdate(appointmentId, AppointmentStatus.CANCELLED);

        if(updateAppointment.getTimeSlotId() != null){
            // TODO: Gọi Doctor Service để mark time slot là available
            System.out.println("Call Doctor Service to release time slot" + updateAppointment.getTimeSlotId());
        }

        return updateAppointment;
    }
}
