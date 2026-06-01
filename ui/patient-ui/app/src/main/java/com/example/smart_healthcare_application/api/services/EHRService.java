package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.EHR;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EHRService {
    @GET("ehr/user/{userId}")
    Call<ApiResponse<EHR>> getEHRByUser(@Path("userId") String userId);
}
