package com.example.appointmentservice.service;

import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import com.example.appointmentservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }
    
    public Optional<Appointment> findById(int appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }
    
    public List<Appointment> findByPatientId(int patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }
    
    public List<Appointment> findByDoctorId(int doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }
    
    public List<Appointment> findByHospitalId(int hospitalId) {
        return appointmentRepository.findByHospitalId(hospitalId);
    }
    
    public List<Appointment> findByDepartmentId(int departmentId) {
        return appointmentRepository.findByDepartmentId(departmentId);
    }
    
    public List<Appointment> findByDepartmentIdAndStatus(int departmentId, AppointmentStatus status) {
        return appointmentRepository.findByDepartmentIdAndStatus(departmentId, status);
    }
    
    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }
    
    public List<Appointment> findByPatientIdAndStatus(int patientId, AppointmentStatus status) {
        return appointmentRepository.findByPatientIdAndStatus(patientId, status);
    }
    
    public List<Appointment> findByDoctorIdAndStatus(int doctorId, AppointmentStatus status) {
        return appointmentRepository.findByDoctorIdAndStatus(doctorId, status);
    }
    
    public List<Appointment> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByAppointmentDateTimeBetween(start, end);
    }
    
    public List<Appointment> findByDoctorAndDateRange(int doctorId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorId, start, end);
    }
    
    public List<Appointment> findByPatientAndDateRange(int patientId, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.findByPatientIdAndAppointmentDateTimeBetween(patientId, start, end);
    }
    
    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    
    public void deleteById(int appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }
    
    // Cập nhật status của appointment
    public Appointment updateStatus(int appointmentId, AppointmentStatus status) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(appointmentId);
        if (appointmentOptional.isPresent()) {
            Appointment appointment = appointmentOptional.get();
            appointment.setStatus(status);
            return appointmentRepository.save(appointment);
        }
        return null;
    }
}

