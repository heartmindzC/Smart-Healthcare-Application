package com.example.notificationservice.controller;

import com.example.notificationservice.client.DoctorServiceClient;
import com.example.notificationservice.client.PatientServiceClient;
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

import org.springframework.http.HttpStatus;

@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService emailService;
    private final NotificationInvoker notificationInvoker;
    private final UserServiceClient userServiceClient;
    private final DoctorServiceClient doctorServiceClient;
    private final PatientServiceClient patientServiceClient;

    @PostMapping("/welcome")
    public ResponseEntity<String> sendWelcomeEmail(@RequestBody UserRegistrationRequest request) {
        try {
            NotificationCommand command = new SendEmailCommand(emailService, request);
            notificationInvoker.executeCommand(command);
            return ResponseEntity.ok("Đã tiếp nhận yêu cầu gửi Welcome Email");
        } catch (RuntimeException e) {
            log.error("[NotificationController] Failed to send welcome email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi Welcome Email thất bại: " + e.getMessage());
        }
    }

    @PostMapping("/otp")
    public ResponseEntity<String> sendOtpEmail(@RequestBody OtpNotificationRequest request) {
        try {
            NotificationCommand command = new SendOtpCommand(emailService, request);
            notificationInvoker.executeCommand(command);
            return ResponseEntity.ok("Yêu cầu gửi email OTP đã được xử lý!");
        } catch (RuntimeException e) {
            log.error("[NotificationController] Failed to send OTP email: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email OTP thất bại: " + e.getMessage());
        }
    }

    // ============== APPOINTMENT NOTIFICATIONS ==============

    /**
     * Gửi email xác nhận lịch hẹn cho bệnh nhân (từ appointment-service).
     */
    @PostMapping("/appointment/patient")
    public ResponseEntity<String> sendAppointmentConfirmationToPatient(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending appointment confirmation to patient: {}", notificationData);
        try {
            NotificationCommand command = new SendConfirmationPatientCommand(emailService, userServiceClient, notificationData, patientServiceClient);
            notificationInvoker.executeCommand(command);
            return ResponseEntity.ok("Đã gửi email xác nhận cho bệnh nhân");
        } catch (RuntimeException e) {
            log.error("[NotificationController] Failed to send appointment confirmation to patient: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email xác nhận cho bệnh nhân thất bại: " + e.getMessage());
        }
    }

    /**
     * Gửi email thông báo lịch hẹn mới cho bác sĩ (từ appointment-service).
     */
    @PostMapping("/appointment/doctor")
    public ResponseEntity<String> sendAppointmentNotificationToDoctor(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending appointment notification to doctor: {}", notificationData);
        try {
            NotificationCommand command = new SendConfirmationDoctorCommand(emailService, userServiceClient, notificationData, doctorServiceClient);
            notificationInvoker.executeCommand(command);
            return ResponseEntity.ok("Đã gửi email thông báo cho bác sĩ");
        } catch (RuntimeException e) {
            log.error("[NotificationController] Failed to send appointment notification to doctor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email thông báo cho bác sĩ thất bại: " + e.getMessage());
        }
    }

    // ============== CANCELLATION NOTIFICATIONS ==============

    /**
     * Gửi email thông báo hủy lịch hẹn cho bệnh nhân.
     */
    @PostMapping("/appointment/cancel/patient")
    public ResponseEntity<String> sendCancellationToPatient(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending cancellation to patient: {}", notificationData);
        try {
            NotificationCommand command = new SendCancellationPatientCommand(emailService, userServiceClient, notificationData, patientServiceClient);
            notificationInvoker.executeCommand(command);
            return ResponseEntity.ok("Đã gửi email hủy cho bệnh nhân");
        } catch (RuntimeException e) {
            log.error("[NotificationController] Failed to send cancellation to patient: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email hủy cho bệnh nhân thất bại: " + e.getMessage());
        }
    }

    /**
     * Gửi email thông báo hủy lịch hẹn cho bác sĩ.
     */
    @PostMapping("/appointment/cancel/doctor")
    public ResponseEntity<String> sendCancellationToDoctor(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending cancellation to doctor: {}", notificationData);
        try {
            NotificationCommand command = new SendCancellationDoctorCommand(emailService, userServiceClient, notificationData, doctorServiceClient);
            notificationInvoker.executeCommand(command);
            return ResponseEntity.ok("Đã gửi email hủy cho bác sĩ");
        } catch (RuntimeException e) {
            log.error("[NotificationController] Failed to send cancellation to doctor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email hủy cho bác sĩ thất bại: " + e.getMessage());
        }
    }

    // ============== COMPLETION NOTIFICATIONS ==============

    /**
     * Gửi email thông báo hoàn thành lịch hẹn cho bệnh nhân.
     */
    @PostMapping("/appointment/complete/patient")
    public ResponseEntity<String> sendCompletionToPatient(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending completion to patient: {}", notificationData);
        try {
            NotificationCommand command = new SendCompletionPatientCommand(emailService, userServiceClient, notificationData, patientServiceClient);
            notificationInvoker.executeCommand(command);
            return ResponseEntity.ok("Đã gửi email hoàn thành cho bệnh nhân");
        } catch (RuntimeException e) {
            log.error("[NotificationController] Failed to send completion to patient: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email hoàn thành cho bệnh nhân thất bại: " + e.getMessage());
        }
    }

    /**
     * Gửi email thông báo hoàn thành lịch hẹn cho bác sĩ.
     */
    @PostMapping("/appointment/complete/doctor")
    public ResponseEntity<String> sendCompletionToDoctor(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending completion to doctor: {}", notificationData);
        try {
            NotificationCommand command = new SendCompletionDoctorCommand(emailService, userServiceClient, notificationData, doctorServiceClient);
            notificationInvoker.executeCommand(command);
            return ResponseEntity.ok("Đã gửi email hoàn thành cho bác sĩ");
        } catch (RuntimeException e) {
            log.error("[NotificationController] Failed to send completion to doctor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Gửi email hoàn thành cho bác sĩ thất bại: " + e.getMessage());
        }
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
