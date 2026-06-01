package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.response.TimeSlotResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TimeSlotApiService {
    /**
     * Lấy tất cả time slots của bác sĩ theo ngày cụ thể qua API Gateway
     */
    @GET("time-slots/doctor/{doctorId}/by-date")
    Call<ApiResponse<List<TimeSlotResponse>>> getTimeSlotsByDoctorAndDate(
            @Path("doctorId") String doctorId,
            @Query("date") String date
    );
}
