package com.example.smart_healthcare_application.api.services;

import com.example.smart_healthcare_application.api.request.PatientRequest;
import com.example.smart_healthcare_application.api.request.UpdatePatientRequest;
import com.example.smart_healthcare_application.api.request.UpdateUserRequest;
import com.example.smart_healthcare_application.api.response.ApiResponse;
import com.example.smart_healthcare_application.models.Patient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PatientService {
    @POST("patients/")
    Call<ApiResponse<Patient>> create(@Body PatientRequest request);

    @GET("patients/userId/{userId}")
    Call<ApiResponse<Patient>> getPatientByUserId(@Path("userId") String userId);

    @PUT("patients/{userId}")
    Call<ApiResponse<Patient>> updatePatient(@Path("userId") String userId,
                                             @Body UpdatePatientRequest request);
}
