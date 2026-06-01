package com.example.smart_healthcare_application.api.repository;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.EHRService;
import com.example.smart_healthcare_application.api.services.UserService;
import com.example.smart_healthcare_application.models.EHR;
import com.example.smart_healthcare_application.utils.ApiErrorMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EHRRepository {
    private final String INTERNET_DISCONNECT = "Lỗi kết nối đến máy chủ: ";
    private EHRService ehrService;
    private static EHRRepository instance;

    private EHRRepository() {
        ehrService = ApiClient.getClient().create(EHRService.class);
    }

    public static EHRRepository getInstance() {
        if (instance == null) {
            synchronized (EHRRepository.class) {
                if (instance == null) {
                    instance = new EHRRepository();
                }
            }
        }
        return instance;
    }

    public void getEHRByUser(String userId, ApiCallback<EHR> callback) {
        ehrService.getEHRByUser(userId).enqueue(new Callback<ApiResponse<EHR>>() {
            @Override
            public void onResponse(Call<ApiResponse<EHR>> call, Response<ApiResponse<EHR>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<EHR>> call, Throwable t) {
                callback.onError(INTERNET_DISCONNECT + t.getMessage());
            }
        });
    }
}
