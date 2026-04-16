package com.example.appointmentservice.handler;

import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.repository.AppointmentRepository;
import com.example.appointmentservice.service.AppointmentEventPublisher;
import com.example.appointmentservice.service.TimeSlotLockService;
import com.example.common_exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Handler xử lý việc CONFIRM appointment
 * 
 * Business Rules:
 * - Chỉ PENDING mới có thể chuyển sang CONFIRMED
 * - Khi confirm: acquire Redis lock + double-check timeslot availability
 * - Mark time slot là đã đặt (not available) sau khi confirm thành công
 * - Gửi email xác nhận cho bệnh nhân
 */
@Component
public class ConfirmAppointmentHandler extends AbstractAppointmentStatusHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConfirmAppointmentHandler.class);

    private static final Set<AppointmentStatus> ALLOWED_PREVIOUS_STATUSES = Set.of(AppointmentStatus.PENDING);

    // Key prefix for storing lockId temporarily during confirmation
    private static final String LOCK_ID_PREFIX = "confirm:lock:";

    @Autowired
    private TimeSlotLockService timeSlotLockService;

    @Autowired
    private AppointmentEventPublisher eventPublisher;

    @Override
    protected void validateTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        super.validateTransition(currentStatus, newStatus);

        if (newStatus != AppointmentStatus.CONFIRMED) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID);
        }

        if (!ALLOWED_PREVIOUS_STATUSES.contains(currentStatus)) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID);
        }
    }

    @Override
    protected void preProcess(Appointment appointment, AppointmentStatus newStatus) {
        super.preProcess(appointment, newStatus);

        if (newStatus == AppointmentStatus.CONFIRMED) {
            String timeSlotId = appointment.getTimeSlotId();

            // Validate timeSlotId exists
            if (timeSlotId == null || timeSlotId.isBlank()) {
                throw new AppException(AppointmentErrorCode.TIMESLOT_ID_NULL);
            }

            // Generate unique lock ID for this confirmation attempt
            String lockId = UUID.randomUUID().toString();

            // Step 1: Acquire distributed lock
            // This prevents race condition when multiple users try to confirm the same time
            // slot
            if (!timeSlotLockService.acquireLock(timeSlotId, lockId)) {
                logger.warn("Failed to acquire lock for timeSlot {} - already locked", timeSlotId);
                throw new AppException(AppointmentErrorCode.TIMESLOT_ALREADY_BOOKED);
            }

            logger.debug("Acquired lock for timeSlot {} with lockId {}", timeSlotId, lockId);

            // Step 2: Double-check time slot availability with Doctor Service
            // This is a second line of defense in case Doctor Service state changed
            try {
                if (!doctorServiceClient.isTimeSlotAvailable(timeSlotId)) {
                    // Release lock and throw exception
                    timeSlotLockService.releaseLock(timeSlotId, lockId);
                    logger.warn("TimeSlot {} is no longer available during confirmation", timeSlotId);
                    throw new AppException(AppointmentErrorCode.TIMESLOT_NOT_AVAILABLE);
                }
            } catch (Exception e) {
                // Release lock if anything goes wrong
                timeSlotLockService.releaseLock(timeSlotId, lockId);
                throw e;
            }

            // Store lockId in appointment for later release in postProcess
            // Using a temporary key that will be cleaned up
            appointment.setNotes(LOCK_ID_PREFIX + lockId);
        }
    }

    @Override
    @Transactional
    protected Appointment doChangeStatus(String appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(AppointmentErrorCode.NOT_FOUND));

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentRepository.save(appointment);
    }

    @Override
    protected void postProcess(Appointment appointment) {
        super.postProcess(appointment);
        logger.info("Post-process CONFIRM for appointment: {}", appointment.getAppointmentId());

        String timeSlotId = appointment.getTimeSlotId();

        // Extract lockId from notes if present
        String storedNotes = appointment.getNotes();
        String lockId = null;

        if (storedNotes != null && storedNotes.startsWith(LOCK_ID_PREFIX)) {
            lockId = storedNotes.substring(LOCK_ID_PREFIX.length());
        }

        // Step 1: Release the distributed lock
        // Note: Doctor timeslot update is now handled by DoctorTimeslotUpdateListener (Observer Pattern)
        // We still release lock here as a safety backup (defensive programming)
        if (lockId != null && timeSlotId != null) {
            try {
                boolean released = timeSlotLockService.releaseLock(timeSlotId, lockId);
                if (released) {
                    logger.debug("Released lock for timeSlot {} with lockId {}", timeSlotId, lockId);
                } else {
                    logger.warn("Failed to release lock for timeSlot {} - lock may have expired", timeSlotId);
                }
            } catch (Exception e) {
                logger.error("Error releasing lock for timeSlot {}: {}", timeSlotId, e);
            }
        }

        // Step 2: Publish confirmation event for notification
        // NotificationListener will send email to patient and doctor
        try {
            eventPublisher.publishConfirmed(appointment, lockId);
        } catch (Exception e) {
            logger.error("Error publishing confirmation event: {}", e.getMessage());
        }

        // Clean up the temporary notes field
        appointment.setNotes(null);
    }
}
