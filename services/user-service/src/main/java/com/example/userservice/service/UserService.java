package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<UserDTO> findByUserId(String userId) {
        Optional<UserDTO> user =  userRepository.findByUserId(userId);
        return user;
    }

    public Optional<UserDTO> findByEmail(String email) {
        Optional<UserDTO> user = userRepository.findByEmail(email);
        return user;
    }
    public Optional<UserDTO> findByPhone(String phone) {
        Optional<UserDTO> user = userRepository.findByPhone(phone);
        return user;
    }
}
