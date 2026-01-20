package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PhoneNumberLoginImp implements LoginMethod {
    private final String type = "phone";
    private final String MESSAGE = "Username or password is wrong";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String password) {
        System.out.println("Login with phone: " + username);
        User user = userRepository.findByPhone(username)
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
