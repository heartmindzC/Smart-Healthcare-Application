package com.example.notificationservice.controller;

import com.example.notificationservice.client.UserServiceClient;
import com.example.notificationservice.command.NotificationCommand;
import com.example.notificationservice.command.NotificationInvoker;
import com.example.notificationservice.command.SendOtpCommand;
import com.example.notificationservice.command.SendEmailCommand;
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

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("title", "Xác Nhận Đặt Lịch Hẹn");
            variables.put("patientName", notificationData.get("patientName"));
            variables.put("message", "Lịch hẹn khám bệnh của bạn đã được xác nhận thành công. Vui lòng đến trước giờ hẹn 15 phút.");
            variables.put("appointmentId", notificationData.get("appointmentId"));
            variables.put("doctorName", notificationData.get("doctorName"));
            variables.put("hospitalName", notificationData.get("hospitalName"));
            variables.put("departmentName", notificationData.get("departmentName"));
            variables.put("reason", notificationData.get("reason"));
            variables.put("appointmentTime", notificationData.get("appointmentDateTime"));

            // Lấy email từ notificationData hoặc từ user-service
            String patientEmail = (String) notificationData.get("patientEmail");
            if (patientEmail == null || patientEmail.isBlank()) {
                String patientId = (String) notificationData.get("patientId");
                patientEmail = getUserEmailById(patientId);
            }

            if (patientEmail != null && !patientEmail.isBlank()) {
                emailService.sendHtmlEmail(
                        patientEmail,
                        "Xác nhận lịch hẹn khám bệnh - Smart Healthcare",
                        "appointment-email",
                        variables
                );
                log.info("[NotificationController] Successfully sent confirmation email to patient: {}", patientEmail);
            } else {
                log.warn("[NotificationController] Patient email not found for patientId: {}", notificationData.get("patientId"));
            }

            return ResponseEntity.ok("Đã gửi email xác nhận cho bệnh nhân");
        } catch (Exception e) {
            log.error("[NotificationController] Failed to send confirmation email to patient: {}", e.getMessage());
            return ResponseEntity.ok("Đã tiếp nhận yêu cầu (gửi email thất bại)");
        }
    }

    /**
     * Gửi email thông báo lịch hẹn mới cho bác sĩ (từ appointment-service).
     */
    @PostMapping("/appointment/doctor")
    public ResponseEntity<String> sendAppointmentNotificationToDoctor(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending appointment notification to doctor: {}", notificationData);

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("title", "Lịch Khám Bệnh Mới");
            variables.put("patientName", "Bác sĩ " + notificationData.get("doctorName"));
            variables.put("message", "Hệ thống vừa ghi nhận một lịch hẹn khám bệnh mới dành cho bác sĩ.");
            variables.put("appointmentId", notificationData.get("appointmentId"));
            variables.put("patientName", notificationData.get("patientName"));
            variables.put("hospitalName", notificationData.get("hospitalName"));
            variables.put("departmentName", notificationData.get("departmentName"));
            variables.put("reason", notificationData.get("reason"));
            variables.put("appointmentTime", notificationData.get("appointmentDateTime"));

            // Lấy email từ notificationData hoặc từ user-service
            String doctorEmail = (String) notificationData.get("doctorEmail");
            if (doctorEmail == null || doctorEmail.isBlank()) {
                String doctorId = (String) notificationData.get("doctorId");
                doctorEmail = getUserEmailById(doctorId);
            }

            if (doctorEmail != null && !doctorEmail.isBlank()) {
                emailService.sendHtmlEmail(
                        doctorEmail,
                        "Bạn có lịch hẹn khám mới - Smart Healthcare",
                        "appointment-email",
                        variables
                );
                log.info("[NotificationController] Successfully sent notification email to doctor: {}", doctorEmail);
            } else {
                log.warn("[NotificationController] Doctor email not found for doctorId: {}", notificationData.get("doctorId"));
            }

            return ResponseEntity.ok("Đã gửi email thông báo cho bác sĩ");
        } catch (Exception e) {
            log.error("[NotificationController] Failed to send notification email to doctor: {}", e.getMessage());
            return ResponseEntity.ok("Đã tiếp nhận yêu cầu (gửi email thất bại)");
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
            Map<String, Object> variables = new HashMap<>();
            variables.put("title", "Lịch Hẹn Đã Bị Hủy");
            variables.put("patientName", notificationData.get("patientName"));
            variables.put("message", "Lịch hẹn khám bệnh của bạn đã bị hủy. Vui lòng đặt lịch hẹn khác nếu cần.");
            variables.put("appointmentId", notificationData.get("appointmentId"));
            variables.put("doctorName", notificationData.get("doctorName"));
            variables.put("hospitalName", notificationData.get("hospitalName"));
            variables.put("departmentName", notificationData.get("departmentName"));
            variables.put("reason", notificationData.get("reason"));

            String patientEmail = (String) notificationData.get("patientEmail");
            if (patientEmail == null || patientEmail.isBlank()) {
                String patientId = (String) notificationData.get("patientId");
                patientEmail = getUserEmailById(patientId);
            }

            if (patientEmail != null && !patientEmail.isBlank()) {
                emailService.sendHtmlEmail(
                        patientEmail,
                        "Thông báo hủy lịch hẹn khám bệnh - Smart Healthcare",
                        "appointment-cancel-email",
                        variables
                );
                log.info("[NotificationController] Successfully sent cancellation email to patient: {}", patientEmail);
            }

            return ResponseEntity.ok("Đã gửi email hủy cho bệnh nhân");
        } catch (Exception e) {
            log.error("[NotificationController] Failed to send cancellation email to patient: {}", e.getMessage());
            return ResponseEntity.ok("Đã tiếp nhận yêu cầu (gửi email thất bại)");
        }
    }

    /**
     * Gửi email thông báo hủy lịch hẹn cho bác sĩ.
     */
    @PostMapping("/appointment/cancel/doctor")
    public ResponseEntity<String> sendCancellationToDoctor(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending cancellation to doctor: {}", notificationData);

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("title", "Lịch Hẹn Đã Bị Hủy");
            variables.put("patientName", "Bác sĩ " + notificationData.get("doctorName"));
            variables.put("message", "Một lịch hẹn khám bệnh đã bị hủy. Timeslot đã được giải phóng.");
            variables.put("appointmentId", notificationData.get("appointmentId"));
            variables.put("patientName", notificationData.get("patientName"));
            variables.put("hospitalName", notificationData.get("hospitalName"));
            variables.put("departmentName", notificationData.get("departmentName"));
            variables.put("reason", notificationData.get("reason"));

            String doctorEmail = (String) notificationData.get("doctorEmail");
            if (doctorEmail == null || doctorEmail.isBlank()) {
                String doctorId = (String) notificationData.get("doctorId");
                doctorEmail = getUserEmailById(doctorId);
            }

            if (doctorEmail != null && !doctorEmail.isBlank()) {
                emailService.sendHtmlEmail(
                        doctorEmail,
                        "Thông báo hủy lịch hẹn khám bệnh - Smart Healthcare",
                        "appointment-cancel-email",
                        variables
                );
                log.info("[NotificationController] Successfully sent cancellation email to doctor: {}", doctorEmail);
            }

            return ResponseEntity.ok("Đã gửi email hủy cho bác sĩ");
        } catch (Exception e) {
            log.error("[NotificationController] Failed to send cancellation email to doctor: {}", e.getMessage());
            return ResponseEntity.ok("Đã tiếp nhận yêu cầu (gửi email thất bại)");
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
            Map<String, Object> variables = new HashMap<>();
            variables.put("title", "Lịch Hẹn Đã Hoàn Thành");
            variables.put("patientName", notificationData.get("patientName"));
            variables.put("message", "Cảm ơn bạn đã sử dụng dịch vụ. Lịch hẹn khám bệnh của bạn đã hoàn thành.");
            variables.put("appointmentId", notificationData.get("appointmentId"));
            variables.put("doctorName", notificationData.get("doctorName"));
            variables.put("hospitalName", notificationData.get("hospitalName"));
            variables.put("departmentName", notificationData.get("departmentName"));

            String patientEmail = (String) notificationData.get("patientEmail");
            if (patientEmail == null || patientEmail.isBlank()) {
                String patientId = (String) notificationData.get("patientId");
                patientEmail = getUserEmailById(patientId);
            }

            if (patientEmail != null && !patientEmail.isBlank()) {
                emailService.sendHtmlEmail(
                        patientEmail,
                        "Lịch hẹn khám bệnh đã hoàn thành - Smart Healthcare",
                        "appointment-complete-email",
                        variables
                );
                log.info("[NotificationController] Successfully sent completion email to patient: {}", patientEmail);
            }

            return ResponseEntity.ok("Đã gửi email hoàn thành cho bệnh nhân");
        } catch (Exception e) {
            log.error("[NotificationController] Failed to send completion email to patient: {}", e.getMessage());
            return ResponseEntity.ok("Đã tiếp nhận yêu cầu (gửi email thất bại)");
        }
    }

    /**
     * Gửi email thông báo hoàn thành lịch hẹn cho bác sĩ.
     */
    @PostMapping("/appointment/complete/doctor")
    public ResponseEntity<String> sendCompletionToDoctor(@RequestBody Map<String, Object> notificationData) {
        log.info("[NotificationController] Sending completion to doctor: {}", notificationData);

        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("title", "Lịch Hẹn Đã Hoàn Thành");
            variables.put("patientName", "Bác sĩ " + notificationData.get("doctorName"));
            variables.put("message", "Lịch hẹn khám bệnh đã được hoàn thành. Cảm ơn bác sĩ đã khám bệnh.");
            variables.put("appointmentId", notificationData.get("appointmentId"));
            variables.put("patientName", notificationData.get("patientName"));
            variables.put("hospitalName", notificationData.get("hospitalName"));
            variables.put("departmentName", notificationData.get("departmentName"));

            String doctorEmail = (String) notificationData.get("doctorEmail");
            if (doctorEmail == null || doctorEmail.isBlank()) {
                String doctorId = (String) notificationData.get("doctorId");
                doctorEmail = getUserEmailById(doctorId);
            }

            if (doctorEmail != null && !doctorEmail.isBlank()) {
                emailService.sendHtmlEmail(
                        doctorEmail,
                        "Lịch hẹn khám bệnh đã hoàn thành - Smart Healthcare",
                        "appointment-complete-email",
                        variables
                );
                log.info("[NotificationController] Successfully sent completion email to doctor: {}", doctorEmail);
            }

            return ResponseEntity.ok("Đã gửi email hoàn thành cho bác sĩ");
        } catch (Exception e) {
            log.error("[NotificationController] Failed to send completion email to doctor: {}", e.getMessage());
            return ResponseEntity.ok("Đã tiếp nhận yêu cầu (gửi email thất bại)");
        }
    }

    // ============== HELPER METHODS ==============

    /**
     * Lấy email của user từ user-service.
     */
    private String getUserEmailById(String userId) {
        return userServiceClient.getUserEmail(userId).orElse(null);
    }

    /**
     * Lấy fullname của user từ user-service.
     */
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
