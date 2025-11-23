package com.example.doctorservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "time_slots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timeSlotId;
    
    private int doctorId;
    
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; // MON, TUE, WED, etc.
    
    private LocalTime startTime;  // VD: 08:00
    private LocalTime endTime;    // VD: 08:30
    
    private Boolean isAvailable;  // true = còn trống, false = đã đặt
    
    private LocalDate specificDate; // null = lặp lại hàng tuần, có giá trị = ngày cụ thể
    
    @CreationTimestamp
    private LocalDateTime createdAt;
}

