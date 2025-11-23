package com.example.appointmentservice.controller;

import com.example.appointmentservice.dto.AppointmentCreateRequest;
import com.example.appointmentservice.dto.AppointmentRequest;
import com.example.appointmentservice.dto.AppointmentResponse;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    
    @Autowired
    private AppointmentService appointmentService;
    
    // Lấy tất cả appointments
    @GetMapping("/all")
    public ResponseEntity<AppointmentResponse> getAllAppointments() {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findAll();
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found");
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointment theo ID
    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable int appointmentId) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            Optional<Appointment> appointmentOptional = appointmentService.findById(appointmentId);
            if (appointmentOptional.isPresent()) {
                response.setStatus(true);
                response.setMessage("Appointment found");
                response.setResult(List.of(appointmentOptional.get()));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Appointment not found with ID: " + appointmentId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointment: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointments theo patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<AppointmentResponse> getAppointmentsByPatient(@PathVariable int patientId) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findByPatientId(patientId);
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found for patient ID: " + patientId);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointments theo doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<AppointmentResponse> getAppointmentsByDoctor(@PathVariable int doctorId) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findByDoctorId(doctorId);
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found for doctor ID: " + doctorId);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointments theo hospital
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<AppointmentResponse> getAppointmentsByHospital(@PathVariable int hospitalId) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findByHospitalId(hospitalId);
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found for hospital ID: " + hospitalId);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointments theo department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<AppointmentResponse> getAppointmentsByDepartment(@PathVariable int departmentId) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findByDepartmentId(departmentId);
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found for department ID: " + departmentId);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointments theo department và status
    @GetMapping("/department/{departmentId}/status/{status}")
    public ResponseEntity<AppointmentResponse> getAppointmentsByDepartmentAndStatus(
            @PathVariable int departmentId,
            @PathVariable AppointmentStatus status) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findByDepartmentIdAndStatus(departmentId, status);
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found for department ID: " + departmentId + " with status: " + status);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointments theo status
    @GetMapping("/status/{status}")
    public ResponseEntity<AppointmentResponse> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findByStatus(status);
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found with status: " + status);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointments theo patient và status
    @GetMapping("/patient/{patientId}/status/{status}")
    public ResponseEntity<AppointmentResponse> getAppointmentsByPatientAndStatus(
            @PathVariable int patientId,
            @PathVariable AppointmentStatus status) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findByPatientIdAndStatus(patientId, status);
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found for patient ID: " + patientId + " with status: " + status);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointments theo doctor và status
    @GetMapping("/doctor/{doctorId}/status/{status}")
    public ResponseEntity<AppointmentResponse> getAppointmentsByDoctorAndStatus(
            @PathVariable int doctorId,
            @PathVariable AppointmentStatus status) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findByDoctorIdAndStatus(doctorId, status);
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found for doctor ID: " + doctorId + " with status: " + status);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy appointments theo khoảng thời gian
    @GetMapping("/date-range")
    public ResponseEntity<AppointmentResponse> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            List<Appointment> appointments = appointmentService.findByDateRange(start, end);
            if (appointments.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No appointments found between " + start + " and " + end);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Appointments found: " + appointments.size());
                response.setResult(appointments);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve appointments: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Tạo appointment mới
    @PostMapping("/create")
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentCreateRequest request) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            Appointment appointment = new Appointment();
            appointment.setDoctorId(request.getDoctorId());
            appointment.setDoctorName(request.getDoctorName());
            appointment.setPatientId(request.getPatientId());
            appointment.setPatientName(request.getPatientName());
            appointment.setHospitalId(request.getHospitalId());
            appointment.setHospitalName(request.getHospitalName());
            appointment.setDepartmentId(request.getDepartmentId());
            appointment.setDepartmentName(request.getDepartmentName());
            appointment.setTimeSlotId(request.getTimeSlotId());
            appointment.setAppointmentDateTime(request.getAppointmentDateTime());
            appointment.setStatus(AppointmentStatus.PENDING);
            appointment.setNotes(request.getNotes());
            appointment.setReason(request.getReason());
            
            Appointment savedAppointment = appointmentService.save(appointment);
            
            // TODO: Gọi Doctor Service để mark time slot là unavailable
            // Implement RestTemplate hoặc WebClient để call Doctor Service API
            
            response.setStatus(true);
            response.setMessage("Appointment created successfully");
            response.setResult(List.of(savedAppointment));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to create appointment: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Cập nhật appointment
    @PutMapping("/update/{appointmentId}")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable int appointmentId,
            @RequestBody AppointmentRequest request) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            Optional<Appointment> appointmentOptional = appointmentService.findById(appointmentId);
            if (appointmentOptional.isPresent()) {
                Appointment appointment = appointmentOptional.get();
                
                if (request.getDoctorId() != null) {
                    appointment.setDoctorId(request.getDoctorId());
                }
                if (request.getDoctorName() != null) {
                    appointment.setDoctorName(request.getDoctorName());
                }
                if (request.getPatientId() != null) {
                    appointment.setPatientId(request.getPatientId());
                }
                if (request.getPatientName() != null) {
                    appointment.setPatientName(request.getPatientName());
                }
                if (request.getHospitalId() != null) {
                    appointment.setHospitalId(request.getHospitalId());
                }
                if (request.getHospitalName() != null) {
                    appointment.setHospitalName(request.getHospitalName());
                }
                if (request.getDepartmentId() != null) {
                    appointment.setDepartmentId(request.getDepartmentId());
                }
                if (request.getDepartmentName() != null) {
                    appointment.setDepartmentName(request.getDepartmentName());
                }
                if (request.getTimeSlotId() != null) {
                    appointment.setTimeSlotId(request.getTimeSlotId());
                }
                if (request.getAppointmentDateTime() != null) {
                    appointment.setAppointmentDateTime(request.getAppointmentDateTime());
                }
                if (request.getStatus() != null) {
                    appointment.setStatus(request.getStatus());
                }
                if (request.getNotes() != null) {
                    appointment.setNotes(request.getNotes());
                }
                if (request.getReason() != null) {
                    appointment.setReason(request.getReason());
                }
                
                Appointment updatedAppointment = appointmentService.save(appointment);
                response.setStatus(true);
                response.setMessage("Appointment updated successfully");
                response.setResult(List.of(updatedAppointment));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Appointment not found with ID: " + appointmentId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to update appointment: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Cập nhật status của appointment
    @PatchMapping("/update-status/{appointmentId}")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable int appointmentId,
            @RequestParam AppointmentStatus status) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            Appointment updatedAppointment = appointmentService.updateStatus(appointmentId, status);
            if (updatedAppointment != null) {
                // Nếu status là CANCELLED, mark time slot là available trở lại
                if (status == AppointmentStatus.CANCELLED && updatedAppointment.getTimeSlotId() != null) {
                    // TODO: Gọi Doctor Service để mark time slot là available
                }
                
                response.setStatus(true);
                response.setMessage("Appointment status updated successfully to: " + status);
                response.setResult(List.of(updatedAppointment));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Appointment not found with ID: " + appointmentId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to update appointment status: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Confirm appointment
    @PatchMapping("/confirm/{appointmentId}")
    public ResponseEntity<AppointmentResponse> confirmAppointment(@PathVariable int appointmentId) {
        return updateAppointmentStatus(appointmentId, AppointmentStatus.CONFIRMED);
    }
    
    // Cancel appointment
    @PatchMapping("/cancel/{appointmentId}")
    public ResponseEntity<AppointmentResponse> cancelAppointment(@PathVariable int appointmentId) {
        return updateAppointmentStatus(appointmentId, AppointmentStatus.CANCELLED);
    }
    
    // Complete appointment
    @PatchMapping("/complete/{appointmentId}")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable int appointmentId) {
        return updateAppointmentStatus(appointmentId, AppointmentStatus.COMPLETED);
    }
    
    // Xóa appointment
    @DeleteMapping("/delete/{appointmentId}")
    public ResponseEntity<AppointmentResponse> deleteAppointment(@PathVariable int appointmentId) {
        AppointmentResponse response = new AppointmentResponse();
        try {
            Optional<Appointment> appointmentOptional = appointmentService.findById(appointmentId);
            if (appointmentOptional.isPresent()) {
                Appointment appointment = appointmentOptional.get();
                
                // Nếu có time slot, mark là available trở lại
                if (appointment.getTimeSlotId() != null) {
                    // TODO: Gọi Doctor Service để mark time slot là available
                }
                
                appointmentService.deleteById(appointmentId);
                response.setStatus(true);
                response.setMessage("Appointment deleted successfully");
                response.setResult(null);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Appointment not found with ID: " + appointmentId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to delete appointment: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
}

