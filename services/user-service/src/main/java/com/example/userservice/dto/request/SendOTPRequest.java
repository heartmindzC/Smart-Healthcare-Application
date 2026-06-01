package com.example.userservice.dto.request;

import lombok.Data;

@Data
public class SendOTPRequest {
    private String email;
    private String fullName;
    private String otp;
}
