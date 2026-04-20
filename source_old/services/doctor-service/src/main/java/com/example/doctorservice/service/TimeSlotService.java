package com.example.doctorservice.service;

import com.example.doctorservice.model.DayOfWeek;
import com.example.doctorservice.model.TimeSlot;
import com.example.doctorservice.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    public List<TimeSlot> findAll() {
        return timeSlotRepository.findAll();
    }
    
    public Optional<TimeSlot> findById(int timeSlotId) {
        return timeSlotRepository.findById(timeSlotId);
    }
    
    public List<TimeSlot> findByDoctorId(int doctorId) {
        return timeSlotRepository.findByDoctorId(doctorId);
    }
    
    public List<TimeSlot> findAvailableSlotsByDoctor(int doctorId) {
        return timeSlotRepository.findByDoctorIdAndIsAvailable(doctorId, true);
    }
    
    public List<TimeSlot> findAvailableSlotsByDoctorAndDate(int doctorId, LocalDate date, DayOfWeek dayOfWeek) {
        return timeSlotRepository.findAvailableSlotsByDoctorAndDate(doctorId, date, dayOfWeek);
    }
    
    public TimeSlot save(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }
    
    public void deleteById(int timeSlotId) {
        timeSlotRepository.deleteById(timeSlotId);
    }
    
    // Update availability (đánh dấu slot đã được đặt hoặc available trở lại)
    public TimeSlot updateAvailability(int timeSlotId, boolean isAvailable) {
        Optional<TimeSlot> timeSlotOptional = timeSlotRepository.findById(timeSlotId);
        if (timeSlotOptional.isPresent()) {
            TimeSlot timeSlot = timeSlotOptional.get();
            timeSlot.setIsAvailable(isAvailable);
            return timeSlotRepository.save(timeSlot);
        }
        return null;
    }
}

