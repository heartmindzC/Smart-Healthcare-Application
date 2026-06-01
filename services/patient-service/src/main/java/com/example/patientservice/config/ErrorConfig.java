package com.example.patientservice.config;

import com.example.common_exception.ErrorCodeRegistry;
import com.example.patientservice.exception.PatientErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {
    @Autowired
    private ErrorCodeRegistry errorCodeRegistry;

    @PostConstruct
    public void registerErrorCodes() {
        errorCodeRegistry.register(PatientErrorCode.class);
    }
}
