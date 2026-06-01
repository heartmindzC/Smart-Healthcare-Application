package com.example.doctorservice.config;

import com.example.common_exception.ErrorCodeRegistry;
import com.example.doctorservice.exception.DoctorErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {
    @Autowired
    private ErrorCodeRegistry errorCodeRegistry;

    @PostConstruct
    public void registerErrorCodes() {
        errorCodeRegistry.register(DoctorErrorCode.class);
    }
}
