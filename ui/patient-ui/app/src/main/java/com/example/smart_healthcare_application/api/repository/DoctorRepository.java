package com.example.smart_healthcare_application.api.repository;

import android.util.Log;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.DoctorService;
import com.example.smart_healthcare_application.models.Doctor;
import com.example.smart_healthcare_application.utils.ApiErrorMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRepository {

    private static final String TAG = "DoctorRepository";
    private final DoctorService doctorService;

    public DoctorRepository() {
        doctorService = ApiClient.getClient().create(DoctorService.class);
    }

    public void getDoctorsByDepartment(String departmentId, ApiCallback<List<Doctor>> callback) {
        doctorService.getDoctorsByDepartment(departmentId).enqueue(new Callback<ApiResponse<List<Doctor>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Doctor>>> call, Response<ApiResponse<List<Doctor>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    String errorMessage = ApiErrorMessage.getErrorMessage(response.errorBody());
                    Log.e(TAG, "getDoctorsByDepartment error: " + errorMessage);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Doctor>>> call, Throwable t) {
                Log.e(TAG, "getDoctorsByDepartment failure: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
