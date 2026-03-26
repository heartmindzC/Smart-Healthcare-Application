package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.reuqest.AppointmentCreateRequest;
import com.example.appointmentservice.dto.reuqest.AppointmentRequest;
import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.appointmentservice.handler.AbstractAppointmentStatusHandler;
import com.example.appointmentservice.handler.AppointmentStatusHandlerFactory;
import com.example.appointmentservice.mapper.AppointmentMapper;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.repository.AppointmentRepository;
import com.example.common_exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private AppointmentStatusHandlerFactory handlerFactory;

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
    
    public List<Appointment> findByPatientAndDateRange(String patientId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByPatientIdAndAppointmentDateTimeBetween(patientId, start, end);
    }
    
    public Appointment save(AppointmentCreateRequest request) {
        Appointment appointment = appointmentMapper.toAppointment(request);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());
        return appointmentRepository.save(appointment);
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
}

