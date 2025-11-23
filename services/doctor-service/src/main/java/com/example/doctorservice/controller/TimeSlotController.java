package com.example.doctorservice.controller;

import com.example.doctorservice.dto.TimeSlotRequest;
import com.example.doctorservice.dto.TimeSlotResponse;
import com.example.doctorservice.model.DayOfWeek;
import com.example.doctorservice.model.TimeSlot;
import com.example.doctorservice.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/time-slots")
public class TimeSlotController {
    
    @Autowired
    private TimeSlotService timeSlotService;
    
    // Lấy tất cả time slots
    @GetMapping("/all")
    public ResponseEntity<TimeSlotResponse> getAllTimeSlots() {
        TimeSlotResponse response = new TimeSlotResponse();
        try {
            List<TimeSlot> timeSlots = timeSlotService.findAll();
            if (timeSlots.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No time slots found");
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Time slots found: " + timeSlots.size());
                response.setResult(timeSlots);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve time slots: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy time slots của một bác sĩ
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<TimeSlotResponse> getTimeSlotsByDoctor(@PathVariable int doctorId) {
        TimeSlotResponse response = new TimeSlotResponse();
        try {
            List<TimeSlot> timeSlots = timeSlotService.findByDoctorId(doctorId);
            if (timeSlots.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No time slots found for doctor ID: " + doctorId);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Time slots found: " + timeSlots.size());
                response.setResult(timeSlots);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve time slots: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy available time slots của một bác sĩ
    @GetMapping("/doctor/{doctorId}/available")
    public ResponseEntity<TimeSlotResponse> getAvailableTimeSlots(@PathVariable int doctorId) {
        TimeSlotResponse response = new TimeSlotResponse();
        try {
            List<TimeSlot> timeSlots = timeSlotService.findAvailableSlotsByDoctor(doctorId);
            if (timeSlots.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No available time slots for doctor ID: " + doctorId);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Available time slots found: " + timeSlots.size());
                response.setResult(timeSlots);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve available time slots: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Lấy available time slots theo ngày cụ thể
    @GetMapping("/doctor/{doctorId}/available-by-date")
    public ResponseEntity<TimeSlotResponse> getAvailableTimeSlotsByDate(
            @PathVariable int doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        TimeSlotResponse response = new TimeSlotResponse();
        try {
            // Convert LocalDate to DayOfWeek
            java.time.DayOfWeek javaDayOfWeek = date.getDayOfWeek();
            DayOfWeek customDayOfWeek = DayOfWeek.valueOf(javaDayOfWeek.toString());
            
            List<TimeSlot> timeSlots = timeSlotService.findAvailableSlotsByDoctorAndDate(
                doctorId, date, customDayOfWeek);
            
            if (timeSlots.isEmpty()) {
                response.setStatus(false);
                response.setMessage("No available time slots for doctor ID: " + doctorId + " on " + date);
                response.setResult(null);
            } else {
                response.setStatus(true);
                response.setMessage("Available time slots found: " + timeSlots.size());
                response.setResult(timeSlots);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to retrieve available time slots: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Tạo time slot mới
    @PostMapping("/create")
    public ResponseEntity<TimeSlotResponse> createTimeSlot(@RequestBody TimeSlotRequest request) {
        TimeSlotResponse response = new TimeSlotResponse();
        try {
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setDoctorId(request.getDoctorId());
            timeSlot.setDayOfWeek(request.getDayOfWeek());
            timeSlot.setStartTime(request.getStartTime());
            timeSlot.setEndTime(request.getEndTime());
            timeSlot.setIsAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true);
            timeSlot.setSpecificDate(request.getSpecificDate());
            
            TimeSlot savedTimeSlot = timeSlotService.save(timeSlot);
            response.setStatus(true);
            response.setMessage("Time slot created successfully");
            response.setResult(List.of(savedTimeSlot));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to create time slot: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Cập nhật time slot
    @PutMapping("/update/{timeSlotId}")
    public ResponseEntity<TimeSlotResponse> updateTimeSlot(
            @PathVariable int timeSlotId,
            @RequestBody TimeSlotRequest request) {
        TimeSlotResponse response = new TimeSlotResponse();
        try {
            Optional<TimeSlot> timeSlotOptional = timeSlotService.findById(timeSlotId);
            if (timeSlotOptional.isPresent()) {
                TimeSlot timeSlot = timeSlotOptional.get();
                
                if (request.getDoctorId() != null) {
                    timeSlot.setDoctorId(request.getDoctorId());
                }
                if (request.getDayOfWeek() != null) {
                    timeSlot.setDayOfWeek(request.getDayOfWeek());
                }
                if (request.getStartTime() != null) {
                    timeSlot.setStartTime(request.getStartTime());
                }
                if (request.getEndTime() != null) {
                    timeSlot.setEndTime(request.getEndTime());
                }
                if (request.getIsAvailable() != null) {
                    timeSlot.setIsAvailable(request.getIsAvailable());
                }
                if (request.getSpecificDate() != null) {
                    timeSlot.setSpecificDate(request.getSpecificDate());
                }
                
                TimeSlot updatedTimeSlot = timeSlotService.save(timeSlot);
                response.setStatus(true);
                response.setMessage("Time slot updated successfully");
                response.setResult(List.of(updatedTimeSlot));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Time slot not found with ID: " + timeSlotId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to update time slot: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Cập nhật availability của time slot
    @PatchMapping("/update-availability/{timeSlotId}")
    public ResponseEntity<TimeSlotResponse> updateAvailability(
            @PathVariable int timeSlotId,
            @RequestParam boolean isAvailable) {
        TimeSlotResponse response = new TimeSlotResponse();
        try {
            TimeSlot updatedTimeSlot = timeSlotService.updateAvailability(timeSlotId, isAvailable);
            if (updatedTimeSlot != null) {
                response.setStatus(true);
                response.setMessage("Time slot availability updated successfully");
                response.setResult(List.of(updatedTimeSlot));
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Time slot not found with ID: " + timeSlotId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to update availability: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
    
    // Xóa time slot
    @DeleteMapping("/delete/{timeSlotId}")
    public ResponseEntity<TimeSlotResponse> deleteTimeSlot(@PathVariable int timeSlotId) {
        TimeSlotResponse response = new TimeSlotResponse();
        try {
            Optional<TimeSlot> timeSlotOptional = timeSlotService.findById(timeSlotId);
            if (timeSlotOptional.isPresent()) {
                timeSlotService.deleteById(timeSlotId);
                response.setStatus(true);
                response.setMessage("Time slot deleted successfully");
                response.setResult(null);
                return ResponseEntity.ok(response);
            } else {
                response.setStatus(false);
                response.setMessage("Time slot not found with ID: " + timeSlotId);
                response.setResult(null);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.setStatus(false);
            response.setMessage("Failed to delete time slot: " + e.getMessage());
            response.setResult(null);
            return ResponseEntity.ok(response);
        }
    }
}

