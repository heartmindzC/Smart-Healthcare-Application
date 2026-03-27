package com.example.smart_healthcare_application.api.api_config;

public interface ApiCallback <T>{
    void onSuccess(T result);
    void onError(String errorMessage);
}
