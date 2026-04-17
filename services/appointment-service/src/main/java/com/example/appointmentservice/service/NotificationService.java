package com.example.appointmentservice.service;

import java.util.Map;

/**
 * Interface cho notification service.
 * Định nghĩa các method gửi notification về appointment cho bệnh nhân và bác sĩ.
 */
public interface NotificationService {

    /**
     * Gửi email xác nhận lịch hẹn cho bệnh nhân.
     */
    void sendAppointmentConfirmationToPatient(Map<String, Object> notificationData);

    /**
     * Gửi email thông báo lịch hẹn mới cho bác sĩ.
     */
    void sendAppointmentNotificationToDoctor(Map<String, Object> notificationData);

    /**
     * Gửi email thông báo hủy lịch hẹn cho bệnh nhân.
     */
    void sendAppointmentCancellationToPatient(Map<String, Object> notificationData);

    /**
     * Gửi email thông báo hủy lịch hẹn cho bác sĩ.
     */
    void sendAppointmentCancellationToDoctor(Map<String, Object> notificationData);

    /**
     * Gửi email thông báo hoàn thành lịch hẹn cho bệnh nhân.
     */
    void sendAppointmentCompletionToPatient(Map<String, Object> notificationData);

    /**
     * Gửi email thông báo hoàn thành lịch hẹn cho bác sĩ.
     */
    void sendAppointmentCompletionToDoctor(Map<String, Object> notificationData);
}