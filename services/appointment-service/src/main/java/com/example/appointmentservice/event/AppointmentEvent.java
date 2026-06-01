package com.example.appointmentservice.event;

/**
 * Base interface for all appointment-related events.
 * All events must provide:
 * - appointmentId: identifies the appointment
 * - occurredAt: timestamp of event creation
 */
public interface AppointmentEvent {
    String getAppointmentId();
    java.time.Instant getOccurredAt();
}
