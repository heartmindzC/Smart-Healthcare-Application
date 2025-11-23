package com.example.appointmentservice.repository;

import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    
    // Tìm appointments theo patient ID
    List<Appointment> findByPatientId(int patientId);
    
    // Tìm appointments theo doctor ID
    List<Appointment> findByDoctorId(int doctorId);
    
    // Tìm appointments theo hospital ID
    List<Appointment> findByHospitalId(int hospitalId);
    
    // Tìm appointments theo department ID
    List<Appointment> findByDepartmentId(int departmentId);
    
    // Tìm appointments theo department và status
    List<Appointment> findByDepartmentIdAndStatus(int departmentId, AppointmentStatus status);
    
    // Tìm appointments theo status
    List<Appointment> findByStatus(AppointmentStatus status);
    
    // Tìm appointments theo patient và status
    List<Appointment> findByPatientIdAndStatus(int patientId, AppointmentStatus status);
    
    // Tìm appointments theo doctor và status
    List<Appointment> findByDoctorIdAndStatus(int doctorId, AppointmentStatus status);
    
    // Tìm appointments theo khoảng thời gian
    List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    // Tìm appointments theo doctor trong khoảng thời gian
    List<Appointment> findByDoctorIdAndAppointmentDateTimeBetween(
        int doctorId, LocalDateTime start, LocalDateTime end);
    
    // Tìm appointments theo patient trong khoảng thời gian
    List<Appointment> findByPatientIdAndAppointmentDateTimeBetween(
        int patientId, LocalDateTime start, LocalDateTime end);
}

