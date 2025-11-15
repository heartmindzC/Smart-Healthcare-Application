package com.example.userservice.controller;

import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.UserListResponse;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/findUserByUserId/{userId}")
    public ResponseEntity<UserResponse> findUserByUserId(@PathVariable("userId") String userId) {
        Optional<UserResponse> user = userService.findByUserId(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            UserResponse notFoundResponse = new UserResponse();
            notFoundResponse.setStatus(false);
            notFoundResponse.setMessage("User not found");
            notFoundResponse.setResult(null);
            return ResponseEntity.ok(notFoundResponse);
        }
    }
    @GetMapping("/findAllUsers")
    public ResponseEntity<UserListResponse> findAllUsers() {
        UserListResponse response = userService.findAllUsers();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest registerRequest) {
        UserResponse response = userService.register(registerRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        UserResponse response = userService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
