package com.example.userservice.service;

import com.example.common_exception.AppException;
import com.example.common_exception.ErrorCode;
import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.request.UpdatePasswordRequest;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.exception.UserErrorCode;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.model.Role;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    //Use @Autowired to inject the share instance into
    @Autowired
    private UserRepository userRepository;

    //Use @Autowired to inject
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Autowired
    @Autowired
    private UserMapper userMapper;

    private final List<LoginMethod>  loginMethods;

    public UserResponse findByUserId(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new AppException(UserErrorCode.NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> findAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponse> userResponses = new ArrayList<>();
        for (User  user : users) {
            userResponses.add(userMapper.toUserResponse(user));
        }

        return userResponses;
    }

    public UserResponse register(RegisterRequest registerRequest) {
        // code check valid
        if (userRepository.existsById(registerRequest.getUserId())) {
            throw new AppException(UserErrorCode.ID_EXISTS);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AppException(UserErrorCode.EMAIL_EXISTS);
        }

        if  (userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new AppException(UserErrorCode.PHONE_EXISTS);
        }

        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.PATIENT);
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository.findByUserId(updatePasswordRequest.getUserId())
                .orElseThrow(()-> new AppException(UserErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new AppException(UserErrorCode.PASSWORD_INVALID);
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public UserResponse login(LoginRequest loginRequest) {
        LoginMethod loginMethod = loginMethods.stream()
                .filter(s -> s.checkType(loginRequest.getType()))
                .findFirst()
                .orElseThrow(()-> new AppException(UserErrorCode.LOGIN_METHOD_INVALID));

        User user = loginMethod.login(loginRequest.getUsername(), loginRequest.getPassword());

        return userMapper.toUserResponse(user);
    }
}
