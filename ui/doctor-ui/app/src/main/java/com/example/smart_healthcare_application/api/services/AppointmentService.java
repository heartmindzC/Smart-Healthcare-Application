package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.Appointment;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AppointmentService {
    @GET("appointments/patient/{patientId}")
    Call<ApiResponse<Appointment>> getAppointmentByUser(@Path("patientId") String patientId);

//    @PATCH("appointments/confirm/{appointmentId}")
//    Call<ApiResponse<Appointment>> confirmAppointment()
}
