package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.Hospital;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HospitalService {
    @GET("hospitals/")
    Call<ApiResponse<List<Hospital>>> getAllHospitals();
}
