package com.example.doctorservice.service;

import com.example.common_exception.AppException;
import com.example.doctorservice.dto.request.TimeSlotRequest;
import com.example.doctorservice.exception.DoctorErrorCode;
import com.example.doctorservice.mapper.TimeSlotMapper;
import com.example.doctorservice.model.DayOfWeek;
import com.example.doctorservice.model.TimeSlot;
import com.example.doctorservice.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private TimeSlotMapper timeSlotMapper;

    public List<TimeSlot> findAll() {
        return timeSlotRepository.findAll();
    }
    
    public TimeSlot findById(String timeSlotId) {
        return timeSlotRepository.findById(timeSlotId).orElseThrow(() -> new AppException(DoctorErrorCode.NOT_FOUND));
    }
    
    public List<TimeSlot> findByDoctorId(String doctorId) {
        return timeSlotRepository.findByDoctorId(doctorId);
    }
    
    public List<TimeSlot> findAvailableSlotsByDoctor(String doctorId) {
        return timeSlotRepository.findByDoctorIdAndIsAvailable(doctorId, true);
    }
    
    public List<TimeSlot> findAvailableSlotsByDoctorAndDate(String doctorId, LocalDate date, DayOfWeek dayOfWeek) {
        return timeSlotRepository.findAvailableSlotsByDoctorAndDate(doctorId, date, dayOfWeek);
    }

    public List<TimeSlot> findByDoctorIdAndSpecificDate(String doctorId, LocalDate specificDate) {
        return timeSlotRepository.findByDoctorIdAndSpecificDate(doctorId, specificDate);
    }
    
    public TimeSlot save(TimeSlotRequest request) {
        Optional<TimeSlot> existingSlotOpt = timeSlotRepository.findByDoctorIdAndSpecificDateAndStartTimeAndEndTime(
                request.getDoctorId(),
                request.getSpecificDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (existingSlotOpt.isPresent()) {
            TimeSlot existingSlot = existingSlotOpt.get();
            if (Boolean.FALSE.equals(existingSlot.getIsAvailable())) {
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
    
    // Update availability (đánh dấu slot đã được đặt hoặc available trở lại)
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
}

