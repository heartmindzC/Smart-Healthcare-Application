package com.example.appointmentservice.event;

import java.time.Instant;

/**
 * Event published when an appointment is cancelled.
 * Contains all necessary data for listeners to process.
 */
public record AppointmentCancelledEvent(
    String eventId,
    String appointmentId,
    String doctorId,
    String doctorName,
    String timeSlotId,
    String patientId,
    String patientName,
    String hospitalName,
    String departmentName,
    String reason,
    Instant occurredAt
) implements AppointmentEvent {

    /**
     * Factory method to create event from Appointment entity.
     */
    public static AppointmentCancelledEvent fromAppointment(
            com.example.appointmentservice.model.Appointment appointment) {

        return new AppointmentCancelledEvent(
            java.util.UUID.randomUUID().toString(),
            appointment.getAppointmentId(),
            appointment.getDoctorId(),
            appointment.getDoctorName(),
            appointment.getTimeSlotId(),
            appointment.getPatientId(),
            appointment.getPatientName(),
            appointment.getHospitalName(),
            appointment.getDepartmentName(),
            appointment.getReason(),
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
