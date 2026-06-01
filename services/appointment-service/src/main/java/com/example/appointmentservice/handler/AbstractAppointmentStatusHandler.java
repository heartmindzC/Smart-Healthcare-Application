package com.example.appointmentservice.handler;

import com.example.appointmentservice.event.AppointmentEvent;
import com.example.appointmentservice.event.AppointmentConfirmedEvent;
// import com.example.appointmentservice.event.AppointmentCancelledEvent;
// import com.example.appointmentservice.event.AppointmentCompletedEvent;
import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.repository.AppointmentRepository;
import com.example.appointmentservice.service.AppointmentEventPublisher;
import com.example.appointmentservice.service.EmailService;
import com.example.appointmentservice.client.DoctorServiceClient;
import com.example.common_exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Abstract base class định nghĩa Template Method cho việc thay đổi trạng thái appointment.
 *
 * Template Method Pattern:
 * changeStatus() là template method gọi các bước theo thứ tự:
 * 1. validateAppointment() - validate appointment tồn tại
 * 2. validateTransition() - validate transition hợp lệ
 * 3. preProcess() - xử lý trước khi đổi status (override nếu cần)
 * 4. doChangeStatus() - thực hiện đổi status (abstract - phải override)
 * 5. postProcess() - xử lý sau khi đổi status (override nếu cần)
 * 6. notifyRelatedServices() - thông báo các service liên quan qua events (Observer Pattern)
 */
public abstract class AbstractAppointmentStatusHandler {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractAppointmentStatusHandler.class);

    @Autowired
    protected AppointmentRepository appointmentRepository;

    @Autowired
    protected EmailService emailService;

    @Autowired
    protected DoctorServiceClient doctorServiceClient;

    @Autowired
    protected AppointmentEventPublisher eventPublisher; // ← NEW: Event Publisher

    /**
     * Template Method - định nghĩa skeleton của thuật toán thay đổi status
     */
    public final Appointment changeStatus(String appointmentId, AppointmentStatus newStatus) { //feature template method
        logger.info("Starting status transition for appointment: {} to status: {}", 
                    appointmentId, newStatus);

        // 1. Validate appointment tồn tại
        Appointment appointment = validateAppointment(appointmentId);

        // 2. Validate status transition hợp lệ
        validateTransition(appointment.getStatus(), newStatus);

        // 3. Pre-process (specific logic before status change)
        preProcess(appointment, newStatus);

        // 4. Thực hiện đổi status - abstract method bắt buộc override
        Appointment updatedAppointment = doChangeStatus(appointmentId, newStatus);

        // 5. Post-process (specific logic after status change)
        postProcess(updatedAppointment);

        // 6. Notify related services
        notifyRelatedServices(updatedAppointment);

        logger.info("Status transition completed successfully for appointment: {}", appointmentId);
        return updatedAppointment;
    }

    /**
     * Validate appointment tồn tại
     */
    protected Appointment validateAppointment(String appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(AppointmentErrorCode.NOT_FOUND));
    }

    /**
     * Validate status transition - cần override để kiểm tra các transition hợp lệ
     * Default: cho phép tất cả transition (override trong subclass để restrict)
     */
    protected void validateTransition(AppointmentStatus currentStatus, AppointmentStatus newStatus) {
        logger.debug("Validating transition from {} to {}", currentStatus, newStatus);
        // Default: cho phép tất cả - override trong subclass để validate cụ thể
    }

    /**
     * Pre-process hook - override nếu cần xử lý trước khi đổi status
     */
    protected void preProcess(Appointment appointment, AppointmentStatus newStatus) {
        logger.debug("Pre-process for appointment: {}, new status: {}", 
                     appointment.getAppointmentId(), newStatus);
        // Default: không làm gì - override trong subclass nếu cần
    }

    /**
     * Abstract method - bắt buộc override để thực hiện đổi status
     */
    protected abstract Appointment doChangeStatus(String appointmentId, AppointmentStatus newStatus);

    /**
     * Post-process hook - override nếu cần xử lý sau khi đổi status
     */
    protected void postProcess(Appointment appointment) {
        logger.debug("Post-process for appointment: {}", appointment.getAppointmentId());
        // Default: không làm gì - override trong subclass nếu cần
    }

    /**
     * Notify related services hook - Publish events for observers (Observer Pattern).
     *
     * Replace direct service calls with event publishing.
     * This decouples status change from side effects (email, doctor update, etc.).
     *
     * Behavior (for CONFIRMED status):
     * 1. Extract lockId from appointment.notes (stored in preProcess)
     * 2. Publish AppointmentConfirmedEvent with lockId
     * 3. Listeners (DoctorListener, EmailListener, ...) will handle the side effects
     *
     * Note: postProcess() in ConfirmAppointmentHandler will still release Redis lock
     * (defensive: listener may fail, handler ensures cleanup)
     */
    protected void notifyRelatedServices(Appointment appointment) {
        logger.debug("Notifying related services for appointment: {}",
                     appointment.getAppointmentId());

        String lockId = extractLockIdFromNotes(appointment.getNotes());

        // Publish event based on status
        if (appointment.getStatus() == AppointmentStatus.CONFIRMED) {
            eventPublisher.publishConfirmed(appointment, lockId);

        } else if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            // eventPublisher.publishCancelled(appointment, lockId); // TODO
            logger.debug("Cancelled event publishing not implemented yet");

        } else if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            // eventPublisher.publishCompleted(appointment); // TODO
            logger.debug("Completed event publishing not implemented yet");
        }

        // Clean up temporary lockId from notes (optional - already done in postProcess)
        // appointment.setNotes(null); // Let postProcess handle cleanup
    }

    /**
     * Extract lockId from notes field (stored as "confirm:lock:{lockId}").
     * Used to pass lock ownership to event listeners.
     */
    private String extractLockIdFromNotes(String notes) {
        if (notes == null) return null;

        final String PREFIX = "confirm:lock:";
        if (notes.startsWith(PREFIX)) {
            return notes.substring(PREFIX.length());
        }
        return null;
    }

    /**
     * Helper method để lấy danh sách status không thể transition ra
     */
    protected boolean isTerminalStatus(AppointmentStatus status) {
        return status == AppointmentStatus.COMPLETED || status == AppointmentStatus.CANCELLED;
    }
}
