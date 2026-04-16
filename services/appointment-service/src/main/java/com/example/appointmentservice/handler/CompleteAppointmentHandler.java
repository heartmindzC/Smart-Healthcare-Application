package com.example.appointmentservice.handler;

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
 * Handler xử lý việc COMPLETE appointment
 * 
 * Business Rules:
 * - Chỉ CONFIRMED mới có thể chuyển sang COMPLETED
 * - PENDING, CANCELLED, COMPLETED không thể complete
 * - Khi complete: cập nhật thông tin khám bệnh (nếu có)
 * - Gửi email thông báo hoàn thành cho bệnh nhân và bác sĩ
 */
@Component
public class CompleteAppointmentHandler extends AbstractAppointmentStatusHandler {

    private static final Logger logger = LoggerFactory.getLogger(CompleteAppointmentHandler.class);

    private static final Set<AppointmentStatus> ALLOWED_PREVIOUS_STATUSES = Set.of(AppointmentStatus.CONFIRMED);

    @Autowired
    private AppointmentEventPublisher eventPublisher;

    @Override
    protected void validateTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        super.validateTransition(currentStatus, newStatus);

        if (newStatus != AppointmentStatus.COMPLETED) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID,
                    "Complete handler chỉ xử lý COMPLETED status");
        }

        if (isTerminalStatus(currentStatus)) {
            throw new AppException(AppointmentErrorCode.STATUS_INVALID,
                    "Không thể hoàn thành appointment đã hoàn thành hoặc đã hủy trước đó. Trạng thái hiện tại: "
                            + currentStatus);
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

    @Override
    protected void postProcess(Appointment appointment) {
        super.postProcess(appointment);
        logger.info("Post-process COMPLETE for appointment: {}", appointment.getAppointmentId());

        // NOTE: KHÔNG release time slot khi COMPLETED
        // Lý do: Slot đã khám xong → KHÔNG ai được đặt slot này nữa
        // Logic availability mới: Slot available KHI và CHỈ KHI không có CONFIRMED appointment
        // COMPLETED appointment không block slot (đã khám xong rồi)
        
        // Publish completion event for notification
        // NotificationListener will send email to patient and doctor
        try {
            eventPublisher.publishCompleted(appointment);
        } catch (Exception e) {
            logger.error("Error publishing completion event: {}", e.getMessage());
        }
    }
}
