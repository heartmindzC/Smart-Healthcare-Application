package com.example.appointmentservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @UuidGenerator
    private String appointmentId;
    
    private String doctorId;
    private String doctorName;
    
    private String patientId;
    private String patientName;
    
    private String hospitalId;
    private String hospitalName;
    
    // Department Information
    private String departmentId;  // Reference to department in hospital-service
    private String departmentName;  // Denormalized for quick access
    
    private String timeSlotId;  // Reference đến time slot trong Doctor Service
    
    private LocalDateTime appointmentDateTime;  // Thời gian hẹn
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // PENDING, CONFIRMED, CANCELLED, COMPLETED
    
    private LocalDateTime pendingCreatedAt;  // Thời điểm chuyển sang PENDING (dùng cho timeout)
    
    private String notes;  // Ghi chú của bệnh nhân
    private String reason; // Lý do khám
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
