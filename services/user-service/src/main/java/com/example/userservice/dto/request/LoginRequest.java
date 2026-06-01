package com.example.userservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = "USERNAME_NULL")
    private String username;
    @NotNull(message = "EMPTY_PASSWORD")
    private String password;
    @NotNull(message = "TYPE_NULL")
    private String type;  //phan biet login email, id, phone
}

