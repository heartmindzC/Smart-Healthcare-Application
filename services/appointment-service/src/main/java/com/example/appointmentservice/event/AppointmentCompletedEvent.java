package com.example.appointmentservice.event;

import java.time.Instant;

/**
 * Event published when an appointment is completed.
 * Contains all necessary data for listeners to process.
 */
public record AppointmentCompletedEvent(
    String eventId,
    String appointmentId,
    String doctorId,
    String doctorName,
    String patientId,
    String patientName,
    String hospitalName,
    String departmentName,
    Instant occurredAt
) implements AppointmentEvent {

    /**
     * Factory method to create event from Appointment entity.
     */
    public static AppointmentCompletedEvent fromAppointment(
            com.example.appointmentservice.model.Appointment appointment) {

        return new AppointmentCompletedEvent(
            java.util.UUID.randomUUID().toString(),
            appointment.getAppointmentId(),
            appointment.getDoctorId(),
            appointment.getDoctorName(),
            appointment.getPatientId(),
            appointment.getPatientName(),
            appointment.getHospitalName(),
            appointment.getDepartmentName(),
            Instant.now()
        );
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String getAppointmentId() {
        return appointmentId;
    }
}
