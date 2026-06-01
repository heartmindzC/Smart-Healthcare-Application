package com.example.appointmentservice.listener;

import com.example.appointmentservice.client.DoctorServiceClient;
import com.example.appointmentservice.event.AppointmentConfirmedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * CRITICAL listener: Updates doctor's timeslot when appointment is confirmed.
 *
 * EXACT BEHAVIOR (giống hệt ConfirmAppointmentHandler.postProcess() phần doctor update):
 * 1. Call doctorServiceClient.markTimeSlotAsBooked(timeSlotId)
 * 2. Nếu success → log debug
 * 3. Nếu fail (return false hoặc exception) → log warning/error, KHÔNG throw
 * 4. KHÔNG release lock (lock đã được release trong handler.postProcess())
 *
 * Order: 1 (chạy đầu tiên - CRITICAL)
 * Synchronous: YES (blocking, không @Async)
 *
 * Why synchronous?
 * - Doctor update là critical business operation
 * - Phải chắc chắn timeslot đã booked trước khi thông báo user
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Order = 1
public class DoctorTimeslotUpdateListener {

    private static final Logger logger = LoggerFactory.getLogger(DoctorTimeslotUpdateListener.class);

    @Autowired
    private DoctorServiceClient doctorServiceClient;

    /**
     * Handle AppointmentConfirmedEvent.
     *
     * LOGIC GIỐNG HỆT phần doctorServiceClient.markTimeSlotAsBooked() trong
     * ConfirmAppointmentHandler.postProcess() (line 127-141 cũ):
     *
     * if (timeSlotId != null) {
     *     try {
     *         boolean success = doctorServiceClient.markTimeSlotAsBooked(timeSlotId);
     *         if (!success) {
     *             logger.warn("Failed to mark time slot as booked: {}", timeSlotId);
     *         } else {
     *             logger.debug("Successfully marked timeSlot {} as booked", timeSlotId);
     *         }
     *     } catch (Exception e) {
     *         logger.error("Error marking time slot as booked: {}", timeSlotId, e);
     *     }
     * }
     *
     * Note: Lock release đã được xử lý trong ConfirmAppointmentHandler.postProcess()
     * trước khi event được publish, nên listener KHÔNG cần release lock.
     */
    @EventListener
    public void onAppointmentConfirmed(AppointmentConfirmedEvent event) {
        String timeSlotId = event.timeSlotId();
        String appointmentId = event.appointmentId();

        logger.info("[DoctorListener] Processing appointment confirmed: {}, timeSlot: {}",
                    appointmentId, timeSlotId);

        // Mark timeslot as booked (exact logic from old code)
        if (timeSlotId != null) {
            try {
                boolean success = doctorServiceClient.markTimeSlotAsBooked(timeSlotId);

                if (!success) {
                    logger.warn("[DoctorListener] Failed to mark time slot as booked: {}", timeSlotId);
                    // KHÔNG throw exception - giữ nguyên behavior cũ
                } else {
                    logger.debug("[DoctorListener] Successfully marked timeSlot {} as booked", timeSlotId);
                }

            } catch (Exception e) {
                logger.error("[DoctorListener] Error marking time slot as booked: {}",
                             timeSlotId, e);
                // KHÔNG throw exception - giữ nguyên behavior cũ
            }
        } else {
            logger.warn("[DoctorListener] timeSlotId is null for appointment: {}", appointmentId);
        }

        logger.info("[DoctorListener] Completed processing for appointment: {}", appointmentId);
    }
}
