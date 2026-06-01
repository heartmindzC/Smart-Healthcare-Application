package com.example.appointmentservice.event;

import java.time.Instant;

/**
 * Event published when an appointment is confirmed.
 * Contains all necessary data for listeners to process.
 */
public record AppointmentConfirmedEvent(
    String eventId,
    String appointmentId,
    String doctorId,
    String timeSlotId,
    String patientId,
    String lockId, // ← Để listener có thể release lock
    Instant occurredAt
) implements AppointmentEvent {
    
    /**
     * Factory method to create event from Appointment entity and lockId.
     */
    public static AppointmentConfirmedEvent fromAppointment(
            com.example.appointmentservice.model.Appointment appointment,
            String lockId) {
        
        return new AppointmentConfirmedEvent(
            java.util.UUID.randomUUID().toString(),
            appointment.getAppointmentId(),
            appointment.getDoctorId(),
            appointment.getTimeSlotId(),
            appointment.getPatientId(),
            lockId,
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
