package com.example.smart_healthcare_application.api.repository;

import android.util.Log;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.AppointmentService;
import com.example.smart_healthcare_application.models.Appointment;
import com.example.smart_healthcare_application.utils.ApiErrorMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class AppointmentRepository {

    private static final String TAG = "AppointmentRepository";
    private final AppointmentService appointmentService;

    public AppointmentRepository() {
        appointmentService = ApiClient.getClient().create(AppointmentService.class);
    }

    public void getAppointmentByUser(String patientId, ApiCallback<List<Appointment>> callback) {
        appointmentService.getAppointmentByUser(patientId).enqueue(new Callback<ApiResponse<List<Appointment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Appointment>>> call, Response<ApiResponse<List<Appointment>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    String errorMessage = ApiErrorMessage.getErrorMessage(response.errorBody());
                    Log.e(TAG, "getAppointmentByUser error: " + errorMessage);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Appointment>>> call, Throwable t) {
                Log.e(TAG, "getAppointmentByUser failure: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    public void cancelAppointment(String appointmentId, ApiCallback<Appointment> callback) {
        appointmentService.cancelAppointment(appointmentId).enqueue(new Callback<ApiResponse<Appointment>>() {
            @Override
            public void onResponse(Call<ApiResponse<Appointment>> call, Response<ApiResponse<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    String errorMessage = ApiErrorMessage.getErrorMessage(response.errorBody());
                    Log.e(TAG, "cancelAppointment error: " + errorMessage);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Appointment>> call, Throwable t) {
                Log.e(TAG, "cancelAppointment failure: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
