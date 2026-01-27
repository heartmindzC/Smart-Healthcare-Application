package com.example.userservice.controller;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.request.UpdatePasswordRequest;
import com.example.userservice.dto.response.ApiResponse;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> findUserByUserId(@PathVariable("userId") String userId) {
        UserResponse user = userService.findByUserId(userId);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .result(user)
                .build();
        return apiResponse;
    }

    @GetMapping("")
    public ApiResponse<List<UserResponse>> findAllUsers() {
        List<UserResponse> response = userService.findAllUsers();
        ApiResponse<List<UserResponse>> apiResponse = ApiResponse.<List<UserResponse>>builder()
                .result(response)
                .build();

        return apiResponse;
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        UserResponse response = userService.register(registerRequest);

        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .result(response)
                .build();

        return apiResponse;
    }

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        UserResponse response = userService.login(loginRequest);
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .result(response)
                .build();
        return apiResponse;
    }

    @PutMapping("/update-password") 
    public ApiResponse<UserResponse> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        UserResponse response = userService.updatePassword(updatePasswordRequest);
        ApiResponse<UserResponse>  apiResponse = ApiResponse.<UserResponse>builder()
                .result(response)
                .build();
        return apiResponse;
    }
}
