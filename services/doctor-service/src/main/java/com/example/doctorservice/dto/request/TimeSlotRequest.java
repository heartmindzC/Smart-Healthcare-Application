package com.example.doctorservice.dto.request;

import com.example.doctorservice.model.DayOfWeek;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotRequest {
    @NotEmpty(message = "DOCTOR_ID_NULL")
    private String doctorId;
    @NotNull(message = "DAY_OF_WEEK_NULL")
    private DayOfWeek dayOfWeek;
    @NotNull(message = "START_TIME_NULL")
    private LocalTime startTime;
    @NotNull(message = "END_TIME_NULL")
    private LocalTime endTime;
    @NotNull(message = "IS_AVAILABLE_NULL")
    private Boolean isAvailable;
    @NotNull(message = "SPECIFIC_DATE_NULL")
    private LocalDate specificDate;
}

