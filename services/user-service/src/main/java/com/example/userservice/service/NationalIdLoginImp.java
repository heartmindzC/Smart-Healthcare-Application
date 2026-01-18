package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NationalIdLoginImp implements LoginMethod {
    private final String type = "nationalId";
    private final String MESSAGE = "Username or password is wrong";
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String username, String password) {
        System.out.println("Login with national ID: " + username);
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException(MESSAGE));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException(MESSAGE);
        }

        return user;
    }

    @Override
    public boolean checkType(String type) {
        return type.equals(this.type);
    }
}
