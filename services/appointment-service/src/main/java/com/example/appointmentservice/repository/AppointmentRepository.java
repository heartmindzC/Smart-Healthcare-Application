package com.example.appointmentservice.repository;

import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    
    // Tìm appointments theo patient ID
    List<Appointment> findByPatientId(String patientId);
    
    // Tìm appointments theo doctor ID
    List<Appointment> findByDoctorId(String doctorId);
    
    // Tìm appointments theo hospital ID
    List<Appointment> findByHospitalId(String hospitalId);
    
    // Tìm appointments theo department ID
    List<Appointment> findByDepartmentId(String departmentId);
    
    // Tìm appointments theo department và status
    List<Appointment> findByDepartmentIdAndStatus(String departmentId, AppointmentStatus status);
    
    // Tìm appointments theo status
    List<Appointment> findByStatus(AppointmentStatus status);
    
    // Tìm appointments theo patient và status
    List<Appointment> findByPatientIdAndStatus(String patientId, AppointmentStatus status);
    
    // Tìm appointments theo doctor và status
    List<Appointment> findByDoctorIdAndStatus(String doctorId, AppointmentStatus status);
    
    // Tìm appointments theo khoảng thời gian
    List<Appointment> findByAppointmentDateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    // Tìm appointments theo doctor trong khoảng thời gian
    List<Appointment> findByDoctorIdAndAppointmentDateTimeBetween(
        String doctorId, LocalDateTime start, LocalDateTime end);
    
    // Tìm appointments theo patient trong khoảng thời gian
    List<Appointment> findByPatientIdAndAppointmentDateTimeBetween(
        String patientId, LocalDateTime start, LocalDateTime end);
    
    // Tìm PENDING appointments đã hết timeout
    List<Appointment> findByStatusAndPendingCreatedAtBefore(
        AppointmentStatus status, LocalDateTime expiredTime);

    /**
     * Kiểm tra xem có CONFIRMED appointment nào trùng doctor + slotId + date không
     * Đây là logic QUERY-DRIVEN AVAILABILITY:
     * - Nếu count > 0 → Slot NOT available (đã có người confirmed)
     * - Nếu count = 0 → Slot IS available (không ai confirmed)
     */
    @Query("SELECT COUNT(a) FROM Appointment a " +
           "WHERE a.doctorId = :doctorId " +
           "AND a.timeSlotId = :timeSlotId " +
           "AND a.status = 'CONFIRMED'")
    long countConfirmedAppointmentsByDoctorAndSlot(
        @Param("doctorId") String doctorId,
        @Param("timeSlotId") String timeSlotId
    );

    /**
     * Kiểm tra nhanh có tồn tại CONFIRMED appointment không
     */
    boolean existsByDoctorIdAndTimeSlotIdAndStatus(
        String doctorId, String timeSlotId, AppointmentStatus status);

    /**
     * Tìm CONFIRMED appointment theo doctor + slot
     */
    Optional<Appointment> findByDoctorIdAndTimeSlotIdAndStatus(
        String doctorId, String timeSlotId, AppointmentStatus status);
}

