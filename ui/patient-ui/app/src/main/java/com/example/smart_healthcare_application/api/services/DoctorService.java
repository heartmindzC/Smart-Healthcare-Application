package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.Doctor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DoctorService {
    @GET("doctors/department/{departmentId}")
    Call<ApiResponse<List<Doctor>>> getDoctorsByDepartment(@Path("departmentId") String departmentId);
}
