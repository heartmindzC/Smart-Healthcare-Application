package com.example.appointmentservice.config;

import com.example.appointmentservice.exception.AppointmentErrorCode;
import com.example.common_exception.ErrorCodeRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {
    @Autowired
    private ErrorCodeRegistry errorCodeRegistry;

    @PostConstruct
    public void registerErrorCodes() {
        errorCodeRegistry.register(AppointmentErrorCode.class);
    }
}
