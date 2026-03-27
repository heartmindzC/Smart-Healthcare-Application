package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.Appointment;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

import java.util.List;

public interface AppointmentService {
    @GET("appointments/patient/{patientId}")
    Call<ApiResponse<List<Appointment>>> getAppointmentByUser(@Path("patientId") String patientId);

    @PATCH("appointments/cancel/{appointmentId}")
    Call<ApiResponse<Appointment>> cancelAppointment(@Path("appointmentId") String appointmentId);
}
