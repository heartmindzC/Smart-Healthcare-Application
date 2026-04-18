package com.example.notificationservice.controller;

import com.example.notificationservice.client.UserServiceClient;
import com.example.notificationservice.command.NotificationCommand;
import com.example.notificationservice.command.NotificationInvoker;
import com.example.notificationservice.command.SendOtpCommand;
import com.example.notificationservice.command.SendEmailCommand;
import com.example.notificationservice.command.SendConfirmationPatientCommand;
import com.example.notificationservice.command.SendConfirmationDoctorCommand;
import com.example.notificationservice.command.SendCancellationPatientCommand;
import com.example.notificationservice.command.SendCancellationDoctorCommand;
import com.example.notificationservice.command.SendCompletionPatientCommand;
import com.example.notificationservice.command.SendCompletionDoctorCommand;
import com.example.notificationservice.dto.AppointmentNotificationRequest;
import com.example.notificationservice.dto.OtpNotificationRequest;
import com.example.notificationservice.dto.UserRegistrationRequest;
import com.example.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService emailService;
    private final NotificationInvoker notificationInvoker;
    private final UserServiceClient userServiceClient;

    @PostMapping("/welcome")
    public ResponseEntity<String> sendWelcomeEmail(@RequestBody UserRegistrationRequest request) {
        NotificationCommand command = new SendEmailCommand(emailService, request);
        notificationInvoker.executeCommand(command);
        return ResponseEntity.ok("Đã tiếp nhận yêu cầu gửi Welcome Email");
    }

    @PostMapping("/otp")
    public ResponseEntity<String> sendOtpEmail(@RequestBody OtpNotificationRequest request) {
        NotificationCommand command = new SendOtpCommand(emailService, request);
        notificationInvoker.executeCommand(command);
        return ResponseEntity.ok("Yêu cầu gửi email OTP đã được xử lý!");
    }

    // ============== APPOINTMENT NOTIFICATIONS ==============

    /**
     * Gửi email xác nhận lịch hẹn cho bệnh nhân (từ appointment-service).
     */
    @PostMapping("/appointment/patient")
    public ResponseEntity<String> sendAppointmentConfirmationToPatient(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending appointment confirmation to patient: {}", notificationData);
        NotificationCommand command = new SendConfirmationPatientCommand(emailService, userServiceClient, notificationData);
        notificationInvoker.executeCommand(command);
        return ResponseEntity.ok("Đã gửi email xác nhận cho bệnh nhân");
    }

    /**
     * Gửi email thông báo lịch hẹn mới cho bác sĩ (từ appointment-service).
     */
    @PostMapping("/appointment/doctor")
    public ResponseEntity<String> sendAppointmentNotificationToDoctor(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending appointment notification to doctor: {}", notificationData);
        NotificationCommand command = new SendConfirmationDoctorCommand(emailService, userServiceClient, notificationData);
        notificationInvoker.executeCommand(command);
        return ResponseEntity.ok("Đã gửi email thông báo cho bác sĩ");
    }

    // ============== CANCELLATION NOTIFICATIONS ==============

    /**
     * Gửi email thông báo hủy lịch hẹn cho bệnh nhân.
     */
    @PostMapping("/appointment/cancel/patient")
    public ResponseEntity<String> sendCancellationToPatient(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending cancellation to patient: {}", notificationData);
        NotificationCommand command = new SendCancellationPatientCommand(emailService, userServiceClient, notificationData);
        notificationInvoker.executeCommand(command);
        return ResponseEntity.ok("Đã gửi email hủy cho bệnh nhân");
    }

    /**
     * Gửi email thông báo hủy lịch hẹn cho bác sĩ.
     */
    @PostMapping("/appointment/cancel/doctor")
    public ResponseEntity<String> sendCancellationToDoctor(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending cancellation to doctor: {}", notificationData);
        NotificationCommand command = new SendCancellationDoctorCommand(emailService, userServiceClient, notificationData);
        notificationInvoker.executeCommand(command);
        return ResponseEntity.ok("Đã gửi email hủy cho bác sĩ");
    }

    // ============== COMPLETION NOTIFICATIONS ==============

    /**
     * Gửi email thông báo hoàn thành lịch hẹn cho bệnh nhân.
     */
    @PostMapping("/appointment/complete/patient")
    public ResponseEntity<String> sendCompletionToPatient(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending completion to patient: {}", notificationData);
        NotificationCommand command = new SendCompletionPatientCommand(emailService, userServiceClient, notificationData);
        notificationInvoker.executeCommand(command);
        return ResponseEntity.ok("Đã gửi email hoàn thành cho bệnh nhân");
    }

    /**
     * Gửi email thông báo hoàn thành lịch hẹn cho bác sĩ.
     */
    @PostMapping("/appointment/complete/doctor")
    public ResponseEntity<String> sendCompletionToDoctor(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending completion to doctor: {}", notificationData);
        NotificationCommand command = new SendCompletionDoctorCommand(emailService, userServiceClient, notificationData);
        notificationInvoker.executeCommand(command);
        return ResponseEntity.ok("Đã gửi email hoàn thành cho bác sĩ");
    }

    // ============== HELPER METHODS ==============

    /**
     * Lấy fullname của user từ user-service.
     */
    @SuppressWarnings("unused")
    private String getUserFullnameById(String userId) {
        return userServiceClient.getUserFullname(userId).orElse(null);
    }

    // Hàm phụ trợ để đóng gói dữ liệu lịch hẹn (legacy - có thể xóa sau)
    @SuppressWarnings("unused")
    private Map<String, Object> createAppointmentVariables(AppointmentNotificationRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("patientName", request.getPatientName());
        variables.put("doctorName", request.getDoctorName());
        variables.put("appointmentTime", request.getAppointmentDateTime());
        variables.put("hospitalName", request.getHospitalName());
        variables.put("departmentName", request.getDepartmentName());
        variables.put("reason", request.getReason());
        return variables;
    }
}
