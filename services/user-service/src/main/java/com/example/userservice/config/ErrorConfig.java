package com.example.userservice.config;

import com.example.common_exception.ErrorCodeRegistry;
import com.example.userservice.exception.UserErrorCode;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorConfig {
    @Autowired
    private ErrorCodeRegistry registry;

    @PostConstruct
    public void init() {
        registry.register(UserErrorCode.class);
    }
}
