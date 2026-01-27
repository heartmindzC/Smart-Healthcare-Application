package com.example.userservice.dto.request;


import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String userId;
    private String oldPassword;
    private String newPassword;
}
