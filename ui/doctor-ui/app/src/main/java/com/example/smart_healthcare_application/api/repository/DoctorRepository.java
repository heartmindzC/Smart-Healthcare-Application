package com.example.smart_healthcare_application.api.repository;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.request.UpdateDoctorRequest;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.DoctorService;
import com.example.smart_healthcare_application.models.Doctor;
import com.example.smart_healthcare_application.utils.ApiErrorMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRepository {
    private final String INTERNET_DISCONNECT = "Lỗi kết nối đến máy chủ: ";
    private DoctorService doctorService;
    private static DoctorRepository instance;

    private DoctorRepository() {
        doctorService = ApiClient.getClient().create(DoctorService.class);
    }

    public static DoctorRepository getInstance() {
        if (instance == null) {
            synchronized (DoctorRepository.class) {
                if (instance == null) {
                    instance = new DoctorRepository();
                }
            }
        }
        return instance;
    }

    public void getDoctorByUserId(String userId, ApiCallback<Doctor> callback) {
        doctorService.getDoctorByUserId(userId).enqueue(new Callback<ApiResponse<Doctor>>() {
            @Override
            public void onResponse(Call<ApiResponse<Doctor>> call, Response<ApiResponse<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Doctor>> call, Throwable t) {
                callback.onError(INTERNET_DISCONNECT + t.getMessage());
            }
        });
    }

    public void updateDoctor(String doctorId, UpdateDoctorRequest request, ApiCallback<Doctor> callback) {
        doctorService.updateDoctor(doctorId, request).enqueue(new Callback<ApiResponse<Doctor>>() {
            @Override
            public void onResponse(Call<ApiResponse<Doctor>> call, Response<ApiResponse<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Doctor>> call, Throwable t) {
                callback.onError(INTERNET_DISCONNECT + t.getMessage());
            }
        });
    }
}
