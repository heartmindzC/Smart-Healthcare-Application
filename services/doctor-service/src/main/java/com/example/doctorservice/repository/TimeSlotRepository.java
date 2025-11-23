package com.example.doctorservice.repository;

import com.example.doctorservice.model.DayOfWeek;
import com.example.doctorservice.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Integer> {
    
    // Tìm tất cả time slots của một bác sĩ
    List<TimeSlot> findByDoctorId(int doctorId);
    
    // Tìm time slots available của bác sĩ
    List<TimeSlot> findByDoctorIdAndIsAvailable(int doctorId, boolean isAvailable);
    
    // Tìm time slots theo ngày cụ thể
    List<TimeSlot> findByDoctorIdAndSpecificDate(int doctorId, LocalDate specificDate);
    
    // Tìm time slots theo day of week (lịch lặp lại)
    List<TimeSlot> findByDoctorIdAndDayOfWeekAndSpecificDateIsNull(int doctorId, DayOfWeek dayOfWeek);
    
    // Tìm available slots cho một ngày cụ thể hoặc theo lịch lặp lại
    @Query("SELECT t FROM TimeSlot t WHERE t.doctorId = :doctorId AND t.isAvailable = true " +
           "AND (t.specificDate = :date OR (t.specificDate IS NULL AND t.dayOfWeek = :dayOfWeek))")
    List<TimeSlot> findAvailableSlotsByDoctorAndDate(
        @Param("doctorId") int doctorId, 
        @Param("date") LocalDate date, 
        @Param("dayOfWeek") DayOfWeek dayOfWeek
    );
}

