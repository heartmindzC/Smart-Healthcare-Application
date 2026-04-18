package com.example.appointmentservice.handler;

import com.example.appointmentservice.client.DoctorServiceClient;
import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.repository.AppointmentRepository;
import com.example.appointmentservice.service.AppointmentEventPublisher;
import com.example.common_exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Handler xử lý việc CANCEL appointment
 * 
 * Business Rules:
 * - PENDING và CONFIRMED đều có thể chuyển sang CANCELLED
 * - COMPLETED và CANCELLED là terminal states (không thể cancel)
 * - Khi cancel: giải phóng time slot (mark as available)
 * - Thông báo cho bệnh nhân về việc hủy
 */
@Component
public class CancelAppointmentHandler extends AbstractAppointmentStatusHandler {

    private static final Logger logger = LoggerFactory.getLogger(CancelAppointmentHandler.class);

    private static final Set<AppointmentStatus> ALLOWED_PREVIOUS_STATUSES = Set.of(AppointmentStatus.PENDING,
            AppointmentStatus.CONFIRMED);

    @Autowired
    private AppointmentEventPublisher eventPublisher;

    @Autowired
    private DoctorServiceClient doctorServiceClient;

    @Override
    protected void validateTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        super.validateTransition(currentStatus, newStatus);

        if (newStatus != AppointmentStatus.CANCELLED) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID);
        }

        if (isTerminalStatus(currentStatus)) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID);
        }

        if (!ALLOWED_PREVIOUS_STATUSES.contains(currentStatus)) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID);
        }
    }

    @Override
    @Transactional
    protected Appointment doChangeStatus(String appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(AppointmentErrorCode.NOT_FOUND));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentRepository.save(appointment);
    }

    @Override
    protected void postProcess(Appointment appointment) {
        super.postProcess(appointment);
        logger.info("Post-process CANCEL for appointment: {}", appointment.getAppointmentId());

        // Giải phóng time slot để hiển thị đúng trên UI
        if (appointment.getTimeSlotId() != null) {
            try {
                boolean released = doctorServiceClient.releaseTimeSlot(appointment.getTimeSlotId());
                if (released) {
                    logger.info("Successfully released time slot: {} for cancelled appointment: {}",
                            appointment.getTimeSlotId(), appointment.getAppointmentId());
                } else {
                    logger.warn("Failed to release time slot: {} for cancelled appointment: {}",
                            appointment.getTimeSlotId(), appointment.getAppointmentId());
                }
            } catch (Exception e) {
                logger.error("Error releasing time slot: {}. Error: {}", appointment.getTimeSlotId(), e.getMessage());
            }
        }

        // Publish cancellation event for notification
        // NotificationListener will send email to patient and doctor
        try {
            eventPublisher.publishCancelled(appointment);
        } catch (Exception e) {
            logger.error("Error publishing cancellation event: {}", e.getMessage());
        }
    }
}
