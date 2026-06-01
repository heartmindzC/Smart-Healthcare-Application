package com.example.doctorservice.service;

import com.example.common_exception.AppException;
import com.example.doctorservice.client.AppointmentServiceClient;
import com.example.doctorservice.dto.request.TimeSlotRequest;
import com.example.doctorservice.exception.DoctorErrorCode;
import com.example.doctorservice.mapper.TimeSlotMapper;
import com.example.doctorservice.model.DayOfWeek;
import com.example.doctorservice.model.TimeSlot;
import com.example.doctorservice.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private TimeSlotMapper timeSlotMapper;
    @Autowired
    private AppointmentServiceClient appointmentServiceClient;

    public List<TimeSlot> findAll() {
        return timeSlotRepository.findAll();
    }
    
    public TimeSlot findById(String timeSlotId) {
        return timeSlotRepository.findById(timeSlotId).orElseThrow(() -> new AppException(DoctorErrorCode.NOT_FOUND));
    }
    
    public List<TimeSlot> findByDoctorId(String doctorId) {
        return timeSlotRepository.findByDoctorId(doctorId);
    }
    
    /**
     * Lấy available slots của bác sĩ
     * Logic mới: QUERY-DRIVEN AVAILABILITY
     * - Slot IS available KHI và CHỈ KHI không có CONFIRMED appointment trùng doctor + slot
     * - isAvailable trên TimeSlot chỉ là backup, availability thật sự query từ Appointment
     */
    public List<TimeSlot> findAvailableSlotsByDoctor(String doctorId) {
        List<TimeSlot> allSlots = timeSlotRepository.findByDoctorId(doctorId);
        
        return allSlots.stream()
            .filter(slot -> appointmentServiceClient.isSlotAvailable(doctorId, slot.getTimeSlotId()))
            .collect(Collectors.toList());
    }
    
    /**
     * Lấy available slots theo ngày cụ thể
     */
    public List<TimeSlot> findAvailableSlotsByDoctorAndDate(String doctorId, LocalDate date, DayOfWeek dayOfWeek) {
        List<TimeSlot> allSlots = timeSlotRepository.findAvailableSlotsByDoctorAndDate(doctorId, date, dayOfWeek);
        
        return allSlots.stream()
            .filter(slot -> appointmentServiceClient.isSlotAvailable(doctorId, slot.getTimeSlotId()))
            .collect(Collectors.toList());
    }

    public List<TimeSlot> findByDoctorIdAndSpecificDate(String doctorId, LocalDate specificDate) {
        return timeSlotRepository.findByDoctorIdAndSpecificDate(doctorId, specificDate);
    }
    
    /**
     * Lưu slot mới hoặc trả về slot đã tồn tại (nếu available)
     */
    @Transactional
    public TimeSlot save(TimeSlotRequest request) {
        Optional<TimeSlot> existingSlotOpt = timeSlotRepository.findByDoctorIdAndSpecificDateAndStartTimeAndEndTime(
                request.getDoctorId(),
                request.getSpecificDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (existingSlotOpt.isPresent()) {
            TimeSlot existingSlot = existingSlotOpt.get();
            // Check availability bằng query từ Appointment (QUERY-DRIVEN)
            if (!appointmentServiceClient.isSlotAvailable(request.getDoctorId(), existingSlot.getTimeSlotId())) {
                throw new AppException(DoctorErrorCode.TIME_SLOT_UNAVAILABLE);
            }
            return existingSlot;
        }

        TimeSlot timeSlot = timeSlotMapper.toTimeSlot(request);
        timeSlot.setCreatedAt(LocalDateTime.now());
        return timeSlotRepository.save(timeSlot);
    }
    
    public void deleteById(String timeSlotId) {
        findById(timeSlotId); // Verify existence, throws exception if not found
        timeSlotRepository.deleteById(timeSlotId);
    }
    
    /**
     * Cập nhật availability (đánh dấu slot đã được đặt hoặc available trở lại)
     * NOTE: Với QUERY-DRIVEN AVAILABILITY, method này chỉ dùng làm backup
     * Availability thật sự được query từ Appointment Service
     */
    @Transactional
    public TimeSlot updateAvailability(String timeSlotId, boolean isAvailable) {
        TimeSlot timeSlot = findById(timeSlotId);
        timeSlot.setIsAvailable(isAvailable);
        return timeSlotRepository.save(timeSlot);
    }

    public TimeSlot update(String id, TimeSlotRequest request) {
        TimeSlot timeSlot = findById(id);

        timeSlot.setDoctorId(request.getDoctorId());
        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setEndTime(request.getEndTime());
        timeSlot.setSpecificDate(request.getSpecificDate());
        timeSlot.setIsAvailable(request.getIsAvailable());
        timeSlot.setDayOfWeek(request.getDayOfWeek());
        return timeSlotRepository.save(timeSlot);
    }

    /**
     * Check slot có available không (QUERY-DRIVEN)
     */
    public boolean isSlotAvailable(String doctorId, String timeSlotId) {
        return appointmentServiceClient.isSlotAvailable(doctorId, timeSlotId);
    }
}

