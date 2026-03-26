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
 * Handler xử lý việc COMPLETE appointment
 * 
 * Business Rules:
 * - Chỉ CONFIRMED mới có thể chuyển sang COMPLETED
 * - PENDING, CANCELLED, COMPLETED không thể complete
 * - Khi complete: cập nhật thông tin khám bệnh (nếu có)
 */
@Component
public class CompleteAppointmentHandler extends AbstractAppointmentStatusHandler {

    private static final Set<AppointmentStatus> ALLOWED_PREVIOUS_STATUSES = 
            Set.of(AppointmentStatus.CONFIRMED);

    @Override
    protected void validateTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        super.validateTransition(currentStatus, newStatus);
        
        if (newStatus != AppointmentStatus.COMPLETED) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID, 
                    "Complete handler chỉ xử lý COMPLETED status");
        }
        
        if (isTerminalStatus(currentStatus)) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID, 
                    "Không thể hoàn thành appointment đã hoàn thành hoặc đã hủy trước đó. Trạng thái hiện tại: " + currentStatus);
        }
        
        if (!ALLOWED_PREVIOUS_STATUSES.contains(currentStatus)) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID, 
                    "Chỉ CONFIRMED appointment mới có thể hoàn thành. Trạng thái hiện tại: " + currentStatus);
        }
    }

    @Override
    @Transactional
    protected Appointment doChangeStatus(String appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(AppointmentErrorCode.NOT_FOUND));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setUpdatedAt(LocalDateTime.now());
        
        return appointmentRepository.save(appointment);
    }
}
