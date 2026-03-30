package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.Department;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DepartmentService {
    @GET("departments/hospital/{hospitalId}")
    Call<ApiResponse<List<Department>>> getDepartmentsByHospital(@Path("hospitalId") String hospitalId);
}
