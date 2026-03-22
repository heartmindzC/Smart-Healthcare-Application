package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.request.LoginRequest;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("users/login")
    Call<ApiResponse<User>> login(@Body LoginRequest loginRequest);
}
