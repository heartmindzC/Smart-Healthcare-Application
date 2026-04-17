package com.example.appointmentservice.listener;

import com.example.appointmentservice.service.NotificationService;
import com.example.appointmentservice.event.AppointmentCancelledEvent;
import com.example.appointmentservice.event.AppointmentCompletedEvent;
import com.example.appointmentservice.event.AppointmentConfirmedEvent;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Listener xử lý việc gửi notification khi có thay đổi appointment.
 * 
 * Gửi email cho:
 * - Bệnh nhân: xác nhận, hủy, hoàn thành lịch hẹn
 * - Bác sĩ: thông báo lịch hẹn mới, hủy, hoàn thành
 * 
 * Order: LOWEST (chạy sau các listener khác)
 * Async: YES (gửi notification bất đồng bộ, không block main flow)
 */
@Component
public class AppointmentNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentNotificationListener.class);

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    @Autowired
    private NotificationService notificationClient;

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * Xử lý sự kiện appointment được xác nhận.
     * Gửi email cho cả bệnh nhân và bác sĩ.
     */
    @Async
    @EventListener
    @Order(100) // Chạy sau các listener có độ ưu tiên cao hơn
    public void onAppointmentConfirmed(AppointmentConfirmedEvent event) {
        logger.info("[NotificationListener] Processing confirmed event for appointment: {}", event.appointmentId());

        try {
            // Load appointment details từ database
            Appointment appointment = appointmentRepository.findById(event.appointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found: " + event.appointmentId()));

            // Lấy thông tin từ appointment
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("appointmentId", event.appointmentId());
            notificationData.put("doctorId", event.doctorId());
            notificationData.put("patientId", event.patientId());
            notificationData.put("timeSlotId", event.timeSlotId());
            notificationData.put("eventType", "CONFIRMED");

            // Lấy thông tin chi tiết từ appointment
            notificationData.put("doctorName", appointment.getDoctorName());
            notificationData.put("patientName", appointment.getPatientName());
            notificationData.put("hospitalName", appointment.getHospitalName());
            notificationData.put("departmentName", appointment.getDepartmentName());
            notificationData.put("reason", appointment.getReason() != null ? appointment.getReason() : "Khám sức khỏe");

            // Format appointment time
            if (appointment.getAppointmentDateTime() != null) {
                notificationData.put("appointmentDateTime", 
                    appointment.getAppointmentDateTime().format(DATE_TIME_FORMATTER));
            }

            // Gửi notification cho bệnh nhân (thông qua appointment-service)
            // Notification service sẽ lấy email từ user-service
            notificationData.put("notificationType", "PATIENT_CONFIRMATION");
            notificationClient.sendAppointmentConfirmationToPatient(notificationData);

            // Gửi notification cho bác sĩ
            notificationData.put("notificationType", "DOCTOR_NEW_APPOINTMENT");
            notificationClient.sendAppointmentNotificationToDoctor(notificationData);

            logger.info("[NotificationListener] Successfully sent confirmation notifications for appointment: {}",
                       event.appointmentId());

        } catch (Exception e) {
            // KHÔNG throw - notification không được fail appointment
            logger.error("[NotificationListener] Failed to send confirmation notifications: {}", e);
        }
    }

    /**
     * Xử lý sự kiện appointment bị hủy.
     * Gửi email thông báo hủy cho cả bệnh nhân và bác sĩ.
     */
    @Async
    @EventListener
    @Order(100)
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        logger.info("[NotificationListener] Processing cancelled event for appointment: {}", event.appointmentId());

        try {
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("appointmentId", event.appointmentId());
            notificationData.put("doctorId", event.doctorId());
            notificationData.put("doctorName", event.doctorName());
            notificationData.put("patientId", event.patientId());
            notificationData.put("patientName", event.patientName());
            notificationData.put("hospitalName", event.hospitalName());
            notificationData.put("departmentName", event.departmentName());
            notificationData.put("reason", event.reason() != null ? event.reason() : "Không có");
            notificationData.put("eventType", "CANCELLED");

            // Gửi notification cho bệnh nhân
            notificationData.put("notificationType", "PATIENT_CANCELLATION");
            notificationClient.sendAppointmentCancellationToPatient(notificationData);

            // Gửi notification cho bác sĩ
            notificationData.put("notificationType", "DOCTOR_CANCELLATION");
            notificationClient.sendAppointmentCancellationToDoctor(notificationData);

            logger.info("[NotificationListener] Successfully sent cancellation notifications for appointment: {}",
                       event.appointmentId());

        } catch (Exception e) {
            logger.error("[NotificationListener] Failed to send cancellation notifications: {}", e);
        }
    }

    /**
     * Xử lý sự kiện appointment hoàn thành.
     * Gửi email thông báo hoàn thành cho cả bệnh nhân và bác sĩ.
     */
    @Async
    @EventListener
    @Order(100)
    public void onAppointmentCompleted(AppointmentCompletedEvent event) {
        logger.info("[NotificationListener] Processing completed event for appointment: {}", event.appointmentId());

        try {
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("appointmentId", event.appointmentId());
            notificationData.put("doctorId", event.doctorId());
            notificationData.put("doctorName", event.doctorName());
            notificationData.put("patientId", event.patientId());
            notificationData.put("patientName", event.patientName());
            notificationData.put("hospitalName", event.hospitalName());
            notificationData.put("departmentName", event.departmentName());
            notificationData.put("eventType", "COMPLETED");

            // Gửi notification cho bệnh nhân
            notificationData.put("notificationType", "PATIENT_COMPLETION");
            notificationClient.sendAppointmentCompletionToPatient(notificationData);

            // Gửi notification cho bác sĩ
            notificationData.put("notificationType", "DOCTOR_COMPLETION");
            notificationClient.sendAppointmentCompletionToDoctor(notificationData);

            logger.info("[NotificationListener] Successfully sent completion notifications for appointment: {}",
                       event.appointmentId());

        } catch (Exception e) {
            logger.error("[NotificationListener] Failed to send completion notifications: {}", e);
        }
    }
}
