package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.Appointment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface AppointmentService {
    @GET("appointments/patient/{patientId}")
    Call<ApiResponse<List<Appointment>>> getAppointmentByUser(@Path("patientId") String patientId);

    @GET("appointments/doctor/{doctorId}")
    Call<ApiResponse<List<Appointment>>> getAppointmentsByDoctor(@Path("doctorId") String doctorId);

    @PATCH("appointments/cancel/{appointmentId}")
    Call<ApiResponse<Void>> cancelAppointment(@Path("appointmentId") String appointmentId);

    @PATCH("appointments/complete/{appointmentId}")
    Call<ApiResponse<Void>> completeAppointment(@Path("appointmentId") String appointmentId);
}
