package com.example.userservice.service;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.UserListResponse;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.model.Role;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<UserResponse> findByUserId(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        return user.map(this::convertToUserResponse);
    }

    public Optional<UserResponse> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(this::convertToUserResponse);
    }

    public Optional<UserResponse> findByPhone(String phone) {
        Optional<User> user = userRepository.findByPhone(phone);
        return user.map(this::convertToUserResponse);
    }

    public UserListResponse findAllUsers() {
        List<User> users = userRepository.findAll();
        
        UserListResponse response = new UserListResponse();
        if (users.isEmpty()) {
            response.setStatus(false);
            response.setMessage("No users found");
            response.setResults(null);
        } else {
            response.setStatus(true);
            response.setMessage("Users found: " + users.size());
            
            // Convert List<User> sang List<UserDTO>
            List<UserDTO> userDTOs = users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
            
            response.setResults(userDTOs);
        }
        return response;
    }

    public UserResponse register(RegisterRequest registerRequest) {
        UserResponse response = new UserResponse();
        
        // Kiểm tra userId đã tồn tại chưa
        if (userRepository.findByUserId(registerRequest.getUserId()).isPresent()) {
            response.setStatus(false);
            response.setMessage("User ID already exists");
            response.setResult(null);
            return response;
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            response.setStatus(false);
            response.setMessage("Email already exists");
            response.setResult(null);
            return response;
        }
        
        // Kiểm tra phone đã tồn tại chưa
        if (userRepository.findByPhone(registerRequest.getPhone()).isPresent()) {
            response.setStatus(false);
            response.setMessage("Phone number already exists");
            response.setResult(null);
            return response;
        }
        
        // Tạo user mới
        User newUser = new User();
        newUser.setUserId(registerRequest.getUserId());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Hash password
        newUser.setPhone(registerRequest.getPhone());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setFullname(registerRequest.getFullname());
        newUser.setAddress(registerRequest.getAddress());
        newUser.setBirth(registerRequest.getBirth());
        newUser.setGender(registerRequest.getGender());
        
        // Set role là PATIENT cho bệnh nhân
        Set<Role> roles = new HashSet<>();
        roles.add(Role.PATIENT);
        newUser.setRoles(roles);
        
        // Lưu user vào database
        User savedUser = userRepository.save(newUser);
        
        // Trả về response
        response.setStatus(true);
        response.setMessage("Registration successful");
        response.setResult(convertToUserDTO(savedUser));
        
        return response;
    }

    public UserResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUserId(loginRequest.getUserId());
        
        UserResponse response = new UserResponse();
        
        if (userOptional.isEmpty()) {
            response.setStatus(false);
            response.setMessage("User not found");
            response.setResult(null);
            return response;
        }
        
        User user = userOptional.get();
        
        // Kiểm tra password: so sánh password từ request với password đã hash trong database
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            response.setStatus(true);
            response.setMessage("Login successful");
            response.setResult(convertToUserDTO(user));
        } else {
            response.setStatus(false);
            response.setMessage("Invalid password");
            response.setResult(null);
        }
        
        return response;
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setStatus(true);
        response.setMessage("User found");
        response.setResult(convertToUserDTO(user));
        return response;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullname(user.getFullname());
        userDTO.setAddress(user.getAddress());
        userDTO.setBirth(user.getBirth());
        userDTO.setGender(user.getGender());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }
}
