package com.example.appointmentservice.scheduler;

import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.repository.AppointmentRepository;
import com.example.appointmentservice.service.AppointmentService;
import com.example.appointmentservice.client.DoctorServiceClient;
import com.example.common_exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduler xử lý các job định kỳ liên quan đến appointments.
 * 
 * Hiện tại xử lý:
 * - PENDING appointment timeout: tự động cancel hoặc confirm những appointment 
 *   đã ở trạng thái PENDING quá lâu.
 */
@Component
public class AppointmentScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentScheduler.class);

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorServiceClient doctorServiceClient;

    @Value("${appointment.pending.timeout-minutes:15}")
    private int pendingTimeoutMinutes;

    @Value("${appointment.auto-confirm.enabled:false}")
    private boolean autoConfirmEnabled;

    /**
     * Xử lý PENDING appointments đã hết timeout.
     * 
     * Chạy mỗi 1 phút (khi giây = 0).
     * 
     * Logic xử lý:
     * 1. Tìm tất cả PENDING appointments đã tạo trước (now - timeout)
     * 2. Với mỗi appointment:
     *    - Nếu auto-confirm = true và timeslot còn available -> tự động confirm
     *    - Ngược lại -> tự động cancel
     */
    @Scheduled(cron = "0 * * * * *")
    public void processExpiredPendingAppointments() {
        logger.info("Starting processExpiredPendingAppointments job...");
        
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(pendingTimeoutMinutes);
        
        List<Appointment> expiredAppointments = appointmentRepository
                .findByStatusAndPendingCreatedAtBefore(AppointmentStatus.PENDING, expiredTime);
        
        if (expiredAppointments.isEmpty()) {
            logger.debug("No expired PENDING appointments found");
            return;
        }
        
        logger.info("Found {} expired PENDING appointments", expiredAppointments.size());
        
        int successCount = 0;
        int failCount = 0;
        int autoConfirmCount = 0;
        
        for (Appointment appointment : expiredAppointments) {
            try {
                boolean processed = processExpiredAppointment(appointment);
                if (processed) {
                    successCount++;
                    if (autoConfirmEnabled) {
                        autoConfirmCount++;
                    }
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                logger.error("Error processing expired appointment: {}", 
                        appointment.getAppointmentId(), e);
            }
        }
        
        logger.info("Completed processExpiredPendingAppointments job. Success: {}, Auto-confirmed: {}, Failed: {}", 
                successCount, autoConfirmCount, failCount);
    }

    /**
     * Xử lý từng appointment đã hết timeout.
     * 
     * @param appointment Appointment đã hết timeout
     * @return true nếu xử lý thành công
     */
    private boolean processExpiredAppointment(Appointment appointment) {
        String timeSlotId = appointment.getTimeSlotId();
        String appointmentId = appointment.getAppointmentId();
        
        logger.debug("Processing expired appointment: {}", appointmentId);
        
        // TH1: Auto-confirm nếu được bật và timeslot còn available
        if (autoConfirmEnabled && timeSlotId != null) {
            try {
                if (doctorServiceClient.isTimeSlotAvailable(timeSlotId)) {
                    appointmentService.updateStatus(appointmentId, AppointmentStatus.CONFIRMED);
                    logger.info("Auto-confirmed expired PENDING appointment: {}", appointmentId);
                    return true;
                } else {
                    logger.debug("TimeSlot {} is not available, will auto-cancel appointment: {}", 
                            timeSlotId, appointmentId);
                }
            } catch (AppException e) {
                if (e.getErrorCode() == AppointmentErrorCode.INVALID_STATUS_TRANSITION) {
                    logger.warn("Appointment {} already processed (status transition invalid), skipping", 
                            appointmentId);
                    return true; // Coi như đã xử lý
                }
                logger.warn("Failed to auto-confirm appointment {}: {}", appointmentId, e.getMessage());
            } catch (Exception e) {
                logger.warn("Error checking time slot availability for appointment {}: {}", 
                        appointmentId, e.getMessage());
                // Tiếp tục để auto-cancel
            }
        }
        
        // TH2: Auto-cancel
        try {
            appointmentService.updateStatus(appointmentId, AppointmentStatus.CANCELLED);
            logger.info("Auto-cancelled expired PENDING appointment: {}", appointmentId);
            return true;
        } catch (AppException e) {
            if (e.getErrorCode() == AppointmentErrorCode.INVALID_STATUS_TRANSITION) {
                logger.warn("Appointment {} already processed (status transition invalid), skipping", 
                        appointmentId);
                return true;
            }
            logger.error("Failed to auto-cancel appointment {}: {}", appointmentId, e.getMessage());
            return false;
        }
    }
}
