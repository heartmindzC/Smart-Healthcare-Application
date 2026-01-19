package com.example.userservice.service;

import com.example.userservice.dto.request.LoginRequest;
import com.example.userservice.dto.request.RegisterRequest;
import com.example.userservice.dto.request.UpdatePasswordRequest;
import com.example.userservice.dto.response.UserResponse;
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
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    private final List<LoginMethod>  loginMethods;

    public UserResponse findByUserId(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

//    public Optional<UserResponses> findByEmail(String email) {
//        Optional<User> user = userRepository.findByEmail(email);
//        return user.map(this::convertToUserResponse);
//    }
//
//    public Optional<UserResponses> findByPhone(String phone) {
//        Optional<User> user = userRepository.findByPhone(phone);
//        return user.map(this::convertToUserResponse);
//    }

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
        // ...

        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.PATIENT);
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = userRepository.findByUserId(updatePasswordRequest.getUserId())
                .orElseThrow(()-> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password doesn't match");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

//    private UserResponses convertToUserResponse(User user) {
//        UserResponses response = new UserResponses();
//        response.setStatus(true);
//        response.setMessage("User found");
//        response.setResult(convertToUserDTO(user));
//        return response;
//    }
//
//    private UserDTO convertToUserDTO(User user) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUserId(user.getUserId());
//        userDTO.setPhone(user.getPhone());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setFullname(user.getFullname());
//        userDTO.setAddress(user.getAddress());
//        userDTO.setBirth(user.getBirth());
//        userDTO.setGender(user.getGender());
//        userDTO.setRoles(user.getRoles());
//        return userDTO;
//    }
//
    public UserResponse login(LoginRequest loginRequest) {
        LoginMethod loginMethod = loginMethods.stream()
                .filter(s -> s.checkType(loginRequest.getType()))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Invalid login type"));

        User user = loginMethod.login(loginRequest.getUsername(), loginRequest.getPassword());

        return userMapper.toUserResponse(user);
    }
}
