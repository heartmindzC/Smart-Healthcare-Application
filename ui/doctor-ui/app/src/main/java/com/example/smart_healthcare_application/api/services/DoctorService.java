package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.request.UpdateDoctorRequest;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.Doctor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DoctorService {
    @GET("doctors/user/{userId}")
    Call<ApiResponse<Doctor>> getDoctorByUserId(@Path("userId") String userId);

    @PUT("doctors/{doctorId}")
    Call<ApiResponse<Doctor>> updateDoctor(@Path("doctorId") String doctorId, @Body UpdateDoctorRequest request);
}
