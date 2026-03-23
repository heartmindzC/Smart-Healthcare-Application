package com.example.userservice.controller;

import com.example.userservice.dto.request.*;
import com.example.userservice.dto.response.ApiResponse;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
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

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable("userId") String userId,
                                                @Valid @RequestBody UserEdittingRequest request) {
        UserResponse response = userService.editUser(userId, request);
        return ApiResponse.<UserResponse>builder()
                .result(response)
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Boolean> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        boolean result = userService.processForgotPassword(request);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<Boolean> verifyOtp(@RequestBody VerifyOtpRequest request) {
        boolean result = userService.verifyOtp(request);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }

    @PostMapping("/reset-password")
    public ApiResponse<Boolean> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean result = userService.resetPassword(request);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }
}
