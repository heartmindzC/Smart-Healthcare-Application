package com.example.smart_healthcare_application.api.repository;

import android.util.Log;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.DepartmentService;
import com.example.smart_healthcare_application.models.Department;
import com.example.smart_healthcare_application.utils.ApiErrorMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentRepository {

    private static final String TAG = "DepartmentRepository";
    private final DepartmentService departmentService;

    public DepartmentRepository() {
        departmentService = ApiClient.getClient().create(DepartmentService.class);
    }

    public void getDepartmentsByHospital(String hospitalId, ApiCallback<List<Department>> callback) {
        departmentService.getDepartmentsByHospital(hospitalId).enqueue(new Callback<ApiResponse<List<Department>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Department>>> call, Response<ApiResponse<List<Department>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    String errorMessage = ApiErrorMessage.getErrorMessage(response.errorBody());
                    Log.e(TAG, "getDepartmentsByHospital error: " + errorMessage);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Department>>> call, Throwable t) {
                Log.e(TAG, "getDepartmentsByHospital failure: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
