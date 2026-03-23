package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.request.ChangePasswordRequest;
import com.example.smart_healthcare_application.api.request.ForgotPasswordRequest;
import com.example.smart_healthcare_application.api.request.LoginRequest;
import com.example.smart_healthcare_application.api.request.RegisterRequest;
import com.example.smart_healthcare_application.api.request.ResetpasswordRequest;
import com.example.smart_healthcare_application.api.request.UpdateUserRequest;
import com.example.smart_healthcare_application.api.request.VerifyOtpRequest;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @POST("users/login")
    Call<ApiResponse<User>> login(@Body LoginRequest loginRequest);

    @POST("users/register")
    Call<ApiResponse<User>> register(@Body RegisterRequest request);

    @POST("users/forgot-password")
    Call<ApiResponse<Boolean>> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("users/verify-otp")
    Call<ApiResponse<Boolean>> verifyOTP(@Body VerifyOtpRequest request);

    @POST("users/reset-password")
    Call<ApiResponse<Boolean>> resetPassword(@Body ResetpasswordRequest request);

    @PUT("users/update-password")
    Call<ApiResponse<User>> changePassword(@Body ChangePasswordRequest request);

    @PUT("users/{userId}")
    Call<ApiResponse<User>> updateUser(@Path ("userId") String userId ,@Body UpdateUserRequest request);
}
