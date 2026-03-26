package com.example.appointmentservice.handler;

import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.repository.AppointmentRepository;
import com.example.common_exception.AppException;
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

    private static final Set<AppointmentStatus> ALLOWED_PREVIOUS_STATUSES = 
            Set.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED);

    @Override
    protected void validateTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        super.validateTransition(currentStatus, newStatus);
        
        if (newStatus != AppointmentStatus.CANCELLED) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID, 
                    "Cancel handler chỉ xử lý CANCELLED status");
        }
        
        if (isTerminalStatus(currentStatus)) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID, 
                    "Không thể hủy appointment đã hoàn thành hoặc đã hủy trước đó. Trạng thái hiện tại: " + currentStatus);
        }
        
        if (!ALLOWED_PREVIOUS_STATUSES.contains(currentStatus)) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID, 
                    "Chỉ PENDING hoặc CONFIRMED appointment mới có thể hủy. Trạng thái hiện tại: " + currentStatus);
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
        
        // Release time slot (mark as available) khi cancel
        if (appointment.getTimeSlotId() != null) {
            boolean success = doctorServiceClient.releaseTimeSlot(appointment.getTimeSlotId());
            if (!success) {
                logger.warn("Failed to release time slot: {}", appointment.getTimeSlotId());
            }
        }
    }
}
