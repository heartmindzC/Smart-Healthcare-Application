package com.example.appointmentservice.service;

import com.example.appointmentservice.event.AppointmentCancelledEvent;
import com.example.appointmentservice.event.AppointmentCompletedEvent;
import com.example.appointmentservice.event.AppointmentConfirmedEvent;
import com.example.appointmentservice.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Central publisher for appointment events.
 * Wraps Spring's ApplicationEventPublisher.
 *
 * Responsibilities:
 * - Create events with necessary data (including lockId for cleanup)
 * - Publish events to Spring's event bus (synchronous, in-process)
 * - Log event publishing for debugging
 *
 * Note: Events are published synchronously within the same thread.
 * Listeners can be async (@Async) if needed.
 */
@Component
public class AppointmentEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentEventPublisher.class);

    @Autowired
    private ApplicationEventPublisher springPublisher;

    /**
     * Publish appointment confirmed event.
     * Called after appointment status is changed to CONFIRMED and DB transaction committed.
     *
     * @param appointment The confirmed appointment (must have status = CONFIRMED)
     * @param lockId The Redis lock ID used during confirmation (may be null if lock not acquired)
     */
    public void publishConfirmed(Appointment appointment, String lockId) {
        AppointmentConfirmedEvent event = AppointmentConfirmedEvent.fromAppointment(appointment, lockId);

        logger.info("Publishing AppointmentConfirmedEvent: {} for appointment: {}, timeSlot: {}, lockId: {}",
                    event.eventId(), event.appointmentId(), event.timeSlotId(), lockId);

        springPublisher.publishEvent(event);
    }

    /**
     * Publish appointment cancelled event.
     * Called after appointment status is changed to CANCELLED.
     *
     * @param appointment The cancelled appointment
     */
    public void publishCancelled(Appointment appointment) {
        AppointmentCancelledEvent event = AppointmentCancelledEvent.fromAppointment(appointment);

        logger.info("Publishing AppointmentCancelledEvent: {} for appointment: {}, doctor: {}, patient: {}",
                    event.eventId(), event.appointmentId(), event.doctorId(), event.patientId());

        springPublisher.publishEvent(event);
    }

    /**
     * Publish appointment completed event.
     * Called after appointment status is changed to COMPLETED.
     *
     * @param appointment The completed appointment
     */
    public void publishCompleted(Appointment appointment) {
        AppointmentCompletedEvent event = AppointmentCompletedEvent.fromAppointment(appointment);

        logger.info("Publishing AppointmentCompletedEvent: {} for appointment: {}, doctor: {}, patient: {}",
                    event.eventId(), event.appointmentId(), event.doctorId(), event.patientId());

        springPublisher.publishEvent(event);
    }
}
