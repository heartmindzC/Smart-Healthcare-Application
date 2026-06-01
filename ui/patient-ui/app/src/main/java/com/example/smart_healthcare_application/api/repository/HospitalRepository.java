package com.example.smart_healthcare_application.api.repository;

import android.util.Log;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.HospitalService;
import com.example.smart_healthcare_application.models.Hospital;
import com.example.smart_healthcare_application.utils.ApiErrorMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalRepository {
    private static final String TAG = "HospitalRepository";
    private final HospitalService hospitalService;

    public HospitalRepository() {
        hospitalService = ApiClient.getClient().create(HospitalService.class);
    }

    public void getAllHospitals(ApiCallback<List<Hospital>> callback) {
        hospitalService.getAllHospitals().enqueue(new Callback<ApiResponse<List<Hospital>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Hospital>>> call, Response<ApiResponse<List<Hospital>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    String errorMessage = ApiErrorMessage.getErrorMessage(response.errorBody());
                    Log.e(TAG, "getAllHospitals error: " + errorMessage);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Hospital>>> call, Throwable t) {
                Log.e(TAG, "getAllHospitals failure: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
