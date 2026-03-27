package com.example.smart_healthcare_application.api.repository;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.request.PatientRequest;
import com.example.smart_healthcare_application.api.request.UpdatePatientRequest;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.PatientService;
import com.example.smart_healthcare_application.models.Patient;
import com.example.smart_healthcare_application.utils.ApiErrorMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PatientRepository {
    private PatientService patientService;
    private static PatientRepository instance;

    private PatientRepository() {
        patientService = ApiClient.getClient().create(PatientService.class);
    }

    public static PatientRepository getInstance() {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new PatientRepository();
                }
            }
        }
        return instance;
    }

    public void createPatient(PatientRequest request, ApiCallback<Patient> callback) {
        patientService.create(request).enqueue(new Callback<ApiResponse<Patient>>() {
            @Override
            public void onResponse(Call<ApiResponse<Patient>> call, Response<ApiResponse<Patient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Patient>> call, Throwable t) {
                callback.onError("Lỗi kết nối đến máy chủ: " + t.getMessage());
            }
        });
    }

    public void getPatientByUserId(String userId, ApiCallback<Patient> callback) {
        patientService.getPatientByUserId(userId).enqueue(new Callback<ApiResponse<Patient>>() {
            @Override
            public void onResponse(Call<ApiResponse<Patient>> call, Response<ApiResponse<Patient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Patient>> call, Throwable t) {
                callback.onError("Lỗi kết nối đến máy chủ: " + t.getMessage());
            }
        });
    }

    public void updatePatient(String userId, UpdatePatientRequest request, ApiCallback<Patient> callback) {
        patientService.updatePatient(userId, request).enqueue(new Callback<ApiResponse<Patient>>() {
            @Override
            public void onResponse(Call<ApiResponse<Patient>> call, Response<ApiResponse<Patient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                }
                else {
                    callback.onError(ApiErrorMessage.getErrorMessage(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Patient>> call, Throwable t) {
                callback.onError("Lỗi kết nối đến máy chủ: " + t.getMessage());
            }
        });
    }
}
