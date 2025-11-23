package com.example.appointmentservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentId;
    
    private int doctorId;
    private String doctorName;
    
    private int patientId;
    private String patientName;
    
    private int hospitalId;
    private String hospitalName;
    
    // Department Information
    private Integer departmentId;  // Reference to department in hospital-service
    private String departmentName;  // Denormalized for quick access
    
    private Integer timeSlotId;  // Reference đến time slot trong Doctor Service
    
    private LocalDateTime appointmentDateTime;  // Thời gian hẹn
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // PENDING, CONFIRMED, CANCELLED, COMPLETED
    
    private String notes;  // Ghi chú của bệnh nhân
    private String reason; // Lý do khám
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
