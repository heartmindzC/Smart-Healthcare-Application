package com.example.doctorservice.controller;

import com.example.doctorservice.dto.request.TimeSlotRequest;
import com.example.doctorservice.dto.response.ApiResponse;
import com.example.doctorservice.model.DayOfWeek;
import com.example.doctorservice.model.TimeSlot;
import com.example.doctorservice.service.TimeSlotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/time-slots")
public class TimeSlotController {
    
    @Autowired
    private TimeSlotService timeSlotService;
    
    // Lấy tất cả time slots
    @GetMapping("/")
    public ApiResponse<List<TimeSlot>> getAllTimeSlots() {
        List<TimeSlot> timeSlots = timeSlotService.findAll();
        return ApiResponse.<List<TimeSlot>>builder()
                .result(timeSlots)
                .build();
    }
    
    // Lấy time slots của một bác sĩ
    @GetMapping("/doctor/{doctorId}")
    public ApiResponse<List<TimeSlot>> getTimeSlotsByDoctor(@PathVariable String doctorId) {
        List<TimeSlot> timeSlots = timeSlotService.findByDoctorId(doctorId);
        return  ApiResponse.<List<TimeSlot>>builder()
                .result(timeSlots)
                .build();
    }
    
    // Lấy available time slots của một bác sĩ
    @GetMapping("/doctor/{doctorId}/available")
    public ApiResponse<List<TimeSlot>> getAvailableTimeSlots(@PathVariable String doctorId) {
        List<TimeSlot> timeSlots = timeSlotService.findAvailableSlotsByDoctor(doctorId);
        return   ApiResponse.<List<TimeSlot>>builder()
                .result(timeSlots)
                .build();
    }
    
    // Lấy available time slots theo ngày cụ thể
    @GetMapping("/doctor/{doctorId}/available-by-date")
    public ApiResponse<List<TimeSlot>> getAvailableTimeSlotsByDate(
            @PathVariable String doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // Convert LocalDate to DayOfWeek
        java.time.DayOfWeek javaDayOfWeek = date.getDayOfWeek();
        DayOfWeek customDayOfWeek = DayOfWeek.valueOf(javaDayOfWeek.toString());

        List<TimeSlot> timeSlots = timeSlotService.findAvailableSlotsByDoctorAndDate(
            doctorId, date, customDayOfWeek);

        return ApiResponse.<List<TimeSlot>>builder()
                .result(timeSlots)
                .build();
    }

    // Lấy tất cả time slots của bác sĩ theo ngày cụ thể (không phân biệt available)
    @GetMapping("/doctor/{doctorId}/by-date")
    public ApiResponse<List<TimeSlot>> getTimeSlotsByDoctorAndDate(
            @PathVariable String doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<TimeSlot> timeSlots = timeSlotService.findByDoctorIdAndSpecificDate(doctorId, date);
        return ApiResponse.<List<TimeSlot>>builder()
                .result(timeSlots)
                .build();
    }
    
    // Lấy time slot theo ID
    @GetMapping("/{timeSlotId}")
    public ApiResponse<TimeSlot> getTimeSlotById(@PathVariable String timeSlotId) {
        TimeSlot timeSlot = timeSlotService.findById(timeSlotId);
        return ApiResponse.<TimeSlot>builder()
                .result(timeSlot)
                .build();
    }
    
    // Tạo time slot mới
    @PostMapping("/")
    public ApiResponse<TimeSlot> createTimeSlot(@Valid @RequestBody TimeSlotRequest request) {
        TimeSlot timeSlot = timeSlotService.save(request);
        return ApiResponse.<TimeSlot>builder()
                .result(timeSlot)
                .build();
    }
    
    // Cập nhật time slot
    @PutMapping("/{timeSlotId}")
    public ApiResponse<TimeSlot> updateTimeSlot(
            @PathVariable String timeSlotId,
            @Valid @RequestBody TimeSlotRequest request) {
        TimeSlot  timeSlot = timeSlotService.update(timeSlotId, request);
        return ApiResponse.<TimeSlot>builder()
                .result(timeSlot)
                .build();
    }
    
    // Cập nhật availability của time slot
    @PatchMapping("/update-availability/{timeSlotId}")
    public ApiResponse<TimeSlot> updateAvailability(
            @PathVariable String timeSlotId,
            @RequestParam boolean isAvailable) {
        TimeSlot timeSlot = timeSlotService.updateAvailability(timeSlotId, isAvailable);
        return ApiResponse.<TimeSlot>builder()
                .result(timeSlot)
                .build();
    }
    
    // Xóa time slot
    @DeleteMapping("/delete/{timeSlotId}")
    public ApiResponse<Void> deleteTimeSlot(@PathVariable String timeSlotId) {
        timeSlotService.deleteById(timeSlotId);
        return ApiResponse.<Void>builder()
                .message("Deleted TimeSlot with id: " + timeSlotId + " successfully")
                .build();
    }
}

