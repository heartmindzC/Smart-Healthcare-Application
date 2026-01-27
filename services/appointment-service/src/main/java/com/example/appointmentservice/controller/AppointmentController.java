package com.example.appointmentservice.controller;

import com.example.appointmentservice.dto.reuqest.AppointmentCreateRequest;
import com.example.appointmentservice.dto.reuqest.AppointmentRequest;
import com.example.appointmentservice.dto.response.ApiResponse;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.service.AppointmentService;
import com.example.appointmentservice.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private com.example.appointmentservice.client.HospitalServiceClient hospitalServiceClient;
    
    // Lấy tất cả appointments
    @GetMapping("/")
    public ApiResponse<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.findAll();
        return ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Lấy appointment theo ID
    @GetMapping("/{appointmentId}")
    public ApiResponse<Appointment> getAppointmentById(@PathVariable String appointmentId) {
        Appointment appointment = appointmentService.findById(appointmentId);
        return ApiResponse.<Appointment>builder()
                .result(appointment)
                .build();
    }
    
    // Lấy appointments theo patient
    @GetMapping("/patient/{patientId}")
    public ApiResponse<List<Appointment>> getAppointmentsByPatient(@PathVariable String patientId) {
        List<Appointment> appointments = appointmentService.findByPatientId(patientId);
        return  ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Lấy appointments theo doctor
    @GetMapping("/doctor/{doctorId}")
    public ApiResponse<List<Appointment>> getAppointmentsByDoctor(@PathVariable String doctorId) {
        List<Appointment> appointments = appointmentService.findByDoctorId(doctorId);
        return ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Lấy appointments theo hospital
    @GetMapping("/hospital/{hospitalId}")
    public ApiResponse<List<Appointment>> getAppointmentsByHospital(@PathVariable String hospitalId) {
        List<Appointment> appointments = appointmentService.findByHospitalId(hospitalId);
        return  ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Lấy appointments theo department
    @GetMapping("/department/{departmentId}")
    public ApiResponse<List<Appointment>> getAppointmentsByDepartment(@PathVariable String departmentId) {
        List<Appointment> appointments = appointmentService.findByDepartmentId(departmentId);
        return ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Lấy appointments theo department và status
    @GetMapping("/department/{departmentId}/status/{status}")
    public ApiResponse<List<Appointment>> getAppointmentsByDepartmentAndStatus(
            @PathVariable String departmentId,
            @PathVariable AppointmentStatus status) {
        List<Appointment> appointments = appointmentService.findByDepartmentIdAndStatus(departmentId, status);
        return  ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Lấy appointments theo status
    @GetMapping("/status/{status}")
    public ApiResponse<List<Appointment>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        List<Appointment> appointments = appointmentService.findByStatus(status);
        return   ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Lấy appointments theo patient và status
    @GetMapping("/patient/{patientId}/status/{status}")
    public ApiResponse<List<Appointment>> getAppointmentsByPatientAndStatus(
            @PathVariable String patientId,
            @PathVariable AppointmentStatus status) {
        List<Appointment> appointments = appointmentService.findByPatientIdAndStatus(patientId, status);
        return   ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Lấy appointments theo doctor và status
    @GetMapping("/doctor/{doctorId}/status/{status}")
    public ApiResponse<List<Appointment>> getAppointmentsByDoctorAndStatus(
            @PathVariable String doctorId,
            @PathVariable AppointmentStatus status) {
        List<Appointment> appointments = appointmentService.findByDoctorIdAndStatus(doctorId, status);
        return   ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Lấy appointments theo khoảng thời gian
    @GetMapping("/date-range")
    public ApiResponse<List<Appointment>> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<Appointment> appointments = appointmentService.findByDateRange(start, end);
        return   ApiResponse.<List<Appointment>>builder()
                .result(appointments)
                .build();
    }
    
    // Tạo appointment mới
    @PostMapping("/")
    public ApiResponse<Appointment> createAppointment(@Valid @RequestBody AppointmentCreateRequest request) {
        Appointment savedAppointment = appointmentService.save(request);
        return  ApiResponse.<Appointment>builder()
                .result(savedAppointment)
                .build();
    }
    
    // Cập nhật appointment
    @PutMapping("/update/{appointmentId}")
    public ApiResponse<Appointment> updateAppointment(
            @PathVariable String appointmentId,
            @Valid @RequestBody AppointmentRequest request) {
        Appointment appointment = appointmentService.update(appointmentId, request);
        return  ApiResponse.<Appointment>builder()
                .result(appointment)
                .build();
    }
    
    // Cập nhật status của appointment
    @PatchMapping("/update-status/{appointmentId}")
    public ApiResponse<Appointment> updateAppointmentStatus(
            @PathVariable String appointmentId,
            @RequestParam AppointmentStatus status) {
        Appointment updatedAppointment = appointmentService.updateStatus(appointmentId, status);
        if (status == AppointmentStatus.CANCELLED && updatedAppointment.getTimeSlotId() != null) {
            // TODO: Gọi Doctor Service để mark time slot là available
        }
        return  ApiResponse.<Appointment>builder()
                .result(updatedAppointment)
                .build();
    }
    
    // Confirm appointment
    @PatchMapping("/confirm/{appointmentId}")
    public ApiResponse<Appointment> confirmAppointment(@PathVariable String appointmentId) {
        return updateAppointmentStatus(appointmentId, AppointmentStatus.CONFIRMED);
    }
    
    // Cancel appointment
    @PatchMapping("/cancel/{appointmentId}")
    public ApiResponse<Appointment> cancelAppointment(@PathVariable String appointmentId) {
        return updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED);
    }
    
    // Complete appointment
    @PatchMapping("/complete/{appointmentId}")
    public ApiResponse<Appointment> completeAppointment(@PathVariable String appointmentId) {
        return updateAppointmentStatus(appointmentId, AppointmentStatus.COMPLETED);
    }
    
    // Xóa appointment
    @DeleteMapping("/delete/{appointmentId}")
    public ApiResponse deleteAppointment(@PathVariable String appointmentId) {

        // Nếu có time slot, mark là available trở lại
//        if (appointment.getTimeSlotId() != null) {
//            // TODO: Gọi Doctor Service để mark time slot là available
//            // Xử lí ở service
//        }
        appointmentService.deleteById(appointmentId);
        return ApiResponse.builder()
                .message("Appointment has been deleted")
                .build();
    }

    /**
     * Endpoint gửi email xác nhận đặt lịch thành công.
     * FE sẽ gọi user-service để lấy email bệnh nhân, sau đó gọi:
     * POST /appointments/send-confirmation-email/{appointmentId}?email=abc@gmail.com
     */
//    @PostMapping("/send-confirmation-email/{appointmentId}")
//    public ResponseEntity<AppointmentResponse> sendConfirmationEmail(
//            @PathVariable String appointmentId,
//            @RequestParam String email) {
//
//        AppointmentResponse response = new AppointmentResponse();
//        try {
//            Optional<Appointment> appointmentOptional = appointmentService.findById(appointmentId);
//            if (appointmentOptional.isEmpty()) {
//                response.setStatus(false);
//                response.setMessage("Appointment not found with ID: " + appointmentId);
//                response.setResult(null);
//                return ResponseEntity.ok(response);
//            }
//
//            Appointment appointment = appointmentOptional.get();
//
//            // Nếu muốn chỉ gửi khi đã CONFIRMED thì bỏ comment đoạn dưới:
//            // if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
//            //     response.setStatus(false);
//            //     response.setMessage("Appointment must be CONFIRMED before sending confirmation email");
//            //     response.setResult(List.of(appointment));
//            //     return ResponseEntity.ok(response);
//            // }
//
//            // Lấy địa chỉ bệnh viện từ Hospital Service (không bắt buộc)
//            String hospitalAddress = null;
//            if (appointment.getHospitalId() > 0) {
//                hospitalAddress = hospitalServiceClient.getHospitalAddress(appointment.getHospitalId())
//                        .orElse(null);
//            }
//
//            emailService.sendAppointmentConfirmationEmail(email, appointment, hospitalAddress);
//
//            response.setStatus(true);
//            response.setMessage("Confirmation email sent successfully to " + email);
//            response.setResult(List.of(appointment));
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            response.setStatus(false);
//            response.setMessage("Failed to send confirmation email: " + e.getMessage());
//            response.setResult(null);
//            return ResponseEntity.ok(response);
//        }
//    }
}
