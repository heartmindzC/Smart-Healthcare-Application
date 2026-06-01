package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.reuqest.AppointmentCreateRequest;
import com.example.appointmentservice.dto.reuqest.AppointmentRequest;
import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.appointmentservice.client.DoctorServiceClient;
import com.example.appointmentservice.handler.AbstractAppointmentStatusHandler;
import com.example.appointmentservice.handler.AppointmentStatusHandlerFactory;
import com.example.appointmentservice.mapper.AppointmentMapper;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.repository.AppointmentRepository;
import com.example.common_exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private AppointmentStatusHandlerFactory handlerFactory;
    @Autowired
    private DoctorServiceClient doctorServiceClient;
    @Autowired
    private TimeSlotLockService timeSlotLockService;

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }
    
    public Appointment findById(String appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppException(AppointmentErrorCode.NOT_FOUND));
    }
    
    public List<Appointment> findByPatientId(String patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    
    public List<Appointment> findByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
    public List<Appointment> findByHospitalId(String hospitalId) {
        return appointmentRepository.findByHospitalId(hospitalId);
    }
    
    public List<Appointment> findByDepartmentId(String departmentId) {
        return appointmentRepository.findByDepartmentId(departmentId);
    }
    
    public List<Appointment> findByDepartmentIdAndStatus(String departmentId, AppointmentStatus status) {
        return appointmentRepository.findByDepartmentIdAndStatus(departmentId, status);
    }
    
    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }
    
    public List<Appointment> findByPatientIdAndStatus(String patientId, AppointmentStatus status) {
        return appointmentRepository.findByPatientIdAndStatus(patientId, status);
    }
    
    public List<Appointment> findByDoctorIdAndStatus(String doctorId, AppointmentStatus status) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
    }
    
    public List<Appointment> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateTimeBetween(start, end);
    }
    
    public List<Appointment> findByDoctorAndDateRange(String doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorId, start, end);
    }
    
    // public List<Appointment> findByPatientAndDateRange(String patientId, LocalDateTime start, LocalDateTime end) {
    //     return appointmentRepository.findByPatientIdAndAppointmentDateTimeBetween(patientId, start, end);
    // }
    
    public Appointment save(AppointmentCreateRequest request) {
        // Tạo lock key dựa trên doctorId và thời gian hẹn
        String lockKey = "appointment:" + request.getDoctorId() + ":" + request.getAppointmentDateTime().toLocalDate() + ":" + request.getAppointmentDateTime().toLocalTime();
        String lockId = UUID.randomUUID().toString();

        // Acquire lock - nếu fail thì reject luôn
        boolean lockAcquired = timeSlotLockService.acquireLock(lockKey, lockId, Duration.ofSeconds(30));
        if (!lockAcquired) {
            throw new AppException(AppointmentErrorCode.TIMESLOT_ALREADY_BOOKED);
        }

        try {
            // Tạo time slot trước khi tạo appointment
            String timeSlotId = doctorServiceClient.createTimeSlot(request.getDoctorId(), request.getAppointmentDateTime());
            
            // Nếu create thất bại (slot đã tồn tại), thử tìm slot đã có
            if (timeSlotId == null) {
                timeSlotId = doctorServiceClient.findExistingTimeSlot(request.getDoctorId(), request.getAppointmentDateTime());
            }

            // Nếu không tìm được slot nào
            if (timeSlotId == null) {
                throw new AppException(AppointmentErrorCode.TIMESLOT_NOT_AVAILABLE);
            }

            // ========== QUERY-DRIVEN AVAILABILITY CHECK ==========
            // Logic mới: Check xem có CONFIRMED appointment nào trùng doctor + slot không
            // - Nếu count > 0 → Slot NOT available (đã có người confirmed)
            // - Nếu count = 0 → Slot IS available
            long confirmedCount = appointmentRepository.countConfirmedAppointmentsByDoctorAndSlot(
                    request.getDoctorId(), timeSlotId);
            
            Appointment appointment = appointmentMapper.toAppointment(request);
            appointment.setTimeSlotId(timeSlotId);
            appointment.setStatus(AppointmentStatus.PENDING);
            appointment.setCreatedAt(LocalDateTime.now());
            appointment.setUpdatedAt(LocalDateTime.now());
            appointment.setPendingCreatedAt(LocalDateTime.now());

            // Lưu trước với status PENDING (để có appointmentId)
            appointment = appointmentRepository.save(appointment);

            // Auto-confirm/cancel dựa trên QUERY-DRIVEN availability - với lock đang giữ
            if (confirmedCount == 0) {
                // Không có CONFIRMED appointment nào → slot available → CONFIRMED
                appointment = updateStatus(appointment.getAppointmentId(), AppointmentStatus.CONFIRMED);
            } else {
                // Đã có người confirmed → slot not available → CANCELLED
                appointment = updateStatus(appointment.getAppointmentId(), AppointmentStatus.CANCELLED);
            }

            return appointment;
        } finally {
            // Luôn release lock
            timeSlotLockService.releaseLock(lockKey, lockId);
        }
    }

    public Appointment update(String id, AppointmentRequest request) {
        Appointment appointment = findById(id);

        appointmentMapper.update(appointment, request);
        appointment.setUpdatedAt(LocalDateTime.now());
        return appointmentRepository.save(appointment);
    }
    
    public void deleteById(String appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }
    // Cập nhật status của appointment
    // public Appointment updateStatus(String appointmentId, AppointmentStatus status) {
    //     Appointment appointment = findById(appointmentId);

    //     appointment.setStatus(status);
    //     return appointmentRepository.save(appointment);
    // }

    // public Appointment performStatusUpdate(String appointmentId, AppointmentStatus status){
    //     Appointment appointment = findById(appointmentId);
    //     appointment.setStatus(status);
    //     appointment.setUpdatedAt(LocalDateTime.now()); //Cập nhật thời gian

    //     return appointmentRepository.save(appointment);
    // }

    /**
     * Cập nhật status của appointment sử dụng Template Pattern Handler
     * 
     * @param appointmentId ID của appointment
     * @param newStatus Status mới cần chuyển đến
     * @return Appointment đã được cập nhật
     */
    public Appointment updateStatus(String appointmentId, AppointmentStatus newStatus) {
        if (appointmentId == null || appointmentId.isBlank()) {
            throw new AppException(AppointmentErrorCode.APPOINTMENT_ID_NULL);
        }
        
        if (newStatus == null) {
            throw new AppException(AppointmentErrorCode.STATUS_NULL);
        }

        // Lấy handler phù hợp và gọi template method
        AbstractAppointmentStatusHandler handler = handlerFactory.getHandler(newStatus);
        return handler.changeStatus(appointmentId, newStatus);
    }

    // ========== QUERY-DRIVEN AVAILABILITY METHODS ==========
    // Dùng cho Doctor Service query availability

    /**
     * Đếm số CONFIRMED appointments trùng doctor + slot
     * Logic: Slot IS available KHI và CHỈ KHI count = 0
     * 
     * @param doctorId ID của bác sĩ
     * @param timeSlotId ID của time slot
     * @return số lượng CONFIRMED appointments (0 hoặc 1)
     */
    public long countConfirmedAppointmentsByDoctorAndSlot(String doctorId, String timeSlotId) {
        return appointmentRepository.countConfirmedAppointmentsByDoctorAndSlot(doctorId, timeSlotId);
    }
}

