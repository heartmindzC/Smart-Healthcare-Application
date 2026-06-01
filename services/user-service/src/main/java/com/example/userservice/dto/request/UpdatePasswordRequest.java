package com.example.userservice.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
    @NotNull(message = "ID_NULL")
    private String userId;
    @NotNull(message = "OLD_PASSWORD_NULL")
    private String oldPassword;
    @NotNull(message = "NEW_PASSWORD_NULL")
    private String newPassword;
}
