package com.example.smart_healthcare_application.api.repository;

import android.util.Log;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.response.TimeSlotResponse;
import com.example.smart_healthcare_application.api.services.TimeSlotApiService;
import com.example.smart_healthcare_application.utils.ApiErrorMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeSlotRepository {
    private static final String TAG = "TimeSlotRepository";
    private final TimeSlotApiService apiService;

    public TimeSlotRepository() {
        apiService = ApiClient.getClient().create(TimeSlotApiService.class);
    }

    public void getTimeSlotsByDoctorAndDate(String doctorId, String date, ApiCallback<List<TimeSlotResponse>> callback) {
        apiService.getTimeSlotsByDoctorAndDate(doctorId, date).enqueue(new Callback<ApiResponse<List<TimeSlotResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<TimeSlotResponse>>> call, Response<ApiResponse<List<TimeSlotResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    String errorMessage = ApiErrorMessage.getErrorMessage(response.errorBody());
                    Log.e(TAG, "getTimeSlots error: " + errorMessage);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<TimeSlotResponse>>> call, Throwable t) {
                Log.e(TAG, "getTimeSlots failure: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
