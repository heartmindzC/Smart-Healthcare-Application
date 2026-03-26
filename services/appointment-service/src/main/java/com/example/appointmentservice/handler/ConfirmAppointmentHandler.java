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
 * Handler xử lý việc CONFIRM appointment
 * 
 * Business Rules:
 * - Chỉ PENDING mới có thể chuyển sang CONFIRMED
 * - Khi confirm: mark time slot là đã đặt (not available)
 * - Gửi email xác nhận cho bệnh nhân
 */
@Component
public class ConfirmAppointmentHandler extends AbstractAppointmentStatusHandler {

    private static final Set<AppointmentStatus> ALLOWED_PREVIOUS_STATUSES = Set.of(AppointmentStatus.PENDING);

    @Override
    protected void validateTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        super.validateTransition(currentStatus, newStatus);
        
        if (newStatus != AppointmentStatus.CONFIRMED) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID, 
                    "Confirm handler chỉ xử lý CONFIRMED status");
        }
        
        if (!ALLOWED_PREVIOUS_STATUSES.contains(currentStatus)) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID, 
                    "Chỉ PENDING appointment mới có thể xác nhận. Trạng thái hiện tại: " + currentStatus);
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
        
        // Mark time slot as booked (not available) khi confirm thành công
        if (appointment.getTimeSlotId() != null) {
            boolean success = doctorServiceClient.markTimeSlotAsBooked(appointment.getTimeSlotId());
            if (!success) {
                logger.warn("Failed to mark time slot as booked: {}", appointment.getTimeSlotId());
            }
        }
    }
}
