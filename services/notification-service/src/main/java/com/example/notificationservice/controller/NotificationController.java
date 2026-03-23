package com.example.notificationservice.controller;

import com.example.notificationservice.dto.OtpNotificationRequest;
import com.example.notificationservice.dto.UserRegistrationRequest;
import com.example.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService emailService;

    @PostMapping("/welcome")
    public ResponseEntity<String> sendWelcomeEmail(@RequestBody UserRegistrationRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", request.getFullName());
        variables.put("userId", request.getUserId());
        variables.put("email", request.getEmail());
        variables.put("phone", request.getPhone());

        emailService.sendHtmlEmail(
                request.getEmail(),
                "Chào mừng bạn đến với hệ thống Smart Healthcare",
                "welcome-email",
                variables
        );
        return ResponseEntity.ok("Đã tiếp nhận yêu cầu gửi Welcome Email");
    }

    @PostMapping("/otp")
    public ResponseEntity<String> sendOtpEmail(@RequestBody OtpNotificationRequest request) {

        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", request.getFullName());
        variables.put("otp", request.getOtp());

        emailService.sendHtmlEmail(
                request.getEmail(),
                "Mã xác thực (OTP) đặt lại mật khẩu - Smart Healthcare",
                "otp-email",
                variables
        );

        return ResponseEntity.ok("Yêu cầu gửi email OTP đã được xử lý!");
    }

    // 2. Gửi Email cho Bệnh nhân khi đặt lịch thành công
//    @PostMapping("/appointment/patient")
//    public ResponseEntity<String> sendAppointmentToPatient(@RequestBody AppointmentNotificationRequest request) {
//        Map<String, Object> variables = createAppointmentVariables(request);
//        variables.put("title", "Xác Nhận Đặt Lịch Hẹn");
//        variables.put("recipientName", request.getPatientName());
//        variables.put("message", "Lịch hẹn khám bệnh của bạn đã được hệ thống xác nhận thành công. Vui lòng đến trước giờ hẹn 15 phút.");
//
//        emailService.sendHtmlEmail(
//                request.getPatientEmail(),
//                "Xác nhận lịch hẹn khám bệnh - Smart Healthcare",
//                "appointment-email",
//                variables
//        );
//        return ResponseEntity.ok("Đã tiếp nhận yêu cầu gửi Email cho Bệnh nhân");
//    }

    // 3. Gửi Email cho Bác sĩ khi có người đặt lịch
//    @PostMapping("/appointment/doctor")
//    public ResponseEntity<String> sendAppointmentToDoctor(@RequestBody AppointmentNotificationRequest request) {
//        Map<String, Object> variables = createAppointmentVariables(request);
//        variables.put("title", "Lịch Khám Bệnh Mới");
//        variables.put("recipientName", "Bác sĩ " + request.getDoctorName());
//        variables.put("message", "Hệ thống vừa ghi nhận một lịch hẹn khám bệnh mới dành cho bác sĩ.");
//
//        emailService.sendHtmlEmail(
//                request.getDoctorEmail(),
//                "Bạn có lịch hẹn khám mới - Smart Healthcare",
//                "appointment-email",
//                variables
//        );
//        return ResponseEntity.ok("Đã tiếp nhận yêu cầu gửi Email cho Bác sĩ");
//    }

    // Hàm phụ trợ để đóng gói dữ liệu lịch hẹn
//    private Map<String, Object> createAppointmentVariables(AppointmentNotificationDto request) {
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("patientName", request.getPatientName());
//        variables.put("doctorName", request.getDoctorName());
//        variables.put("appointmentTime", request.getAppointmentDateTime()); // Định dạng chuỗi sẵn từ Service gọi sang
//        variables.put("hospitalName", request.getHospitalName());
//        variables.put("departmentName", request.getDepartmentName());
//        variables.put("reason", request.getReason());
//        return variables;
//    }
}