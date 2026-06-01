package com.example.smart_healthcare_application.api.repository;

import com.example.smart_healthcare_application.api.api_config.ApiCallback;
import com.example.smart_healthcare_application.api.api_config.ApiClient;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.api.services.AppointmentService;
import com.example.smart_healthcare_application.models.Appointment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentRepository {
    private static AppointmentRepository instance;
    private AppointmentService appointmentService;

    private AppointmentRepository() {
        appointmentService = ApiClient.getClient().create(AppointmentService.class);
    }

    public static synchronized AppointmentRepository getInstance() {
        if (instance == null) {
            instance = new AppointmentRepository();
        }
        return instance;
    }

    public void getAppointmentsByDoctor(String doctorId, ApiCallback<List<Appointment>> callback) {
        appointmentService.getAppointmentsByDoctor(doctorId).enqueue(new Callback<ApiResponse<List<Appointment>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Appointment>>> call, Response<ApiResponse<List<Appointment>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getResult());
                } else {
                    callback.onError("Lỗi khi tải lịch làm việc");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Appointment>>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void cancelAppointment(String appointmentId, ApiCallback<Void> callback) {
        appointmentService.cancelAppointment(appointmentId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Lỗi khi hủy lịch hẹn");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void completeAppointment(String appointmentId, ApiCallback<Void> callback) {
        appointmentService.completeAppointment(appointmentId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onError("Lỗi khi hoàn thành lịch hẹn");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
