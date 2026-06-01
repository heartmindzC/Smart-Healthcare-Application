package com.example.userservice.service;

import com.example.common_exception.AppException;
import com.example.common_exception.ErrorCode;
import com.example.userservice.exception.UserErrorCode;
import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NationalIdLoginImp implements LoginMethod {
    private final String type = "nationalId";
    private final ErrorCode MESSAGE = UserErrorCode.LOGIN_FAILED;
    
    //Use @Autowired to inject the share instance into
    @Autowired
    private UserRepository userRepository;

    //Use @Autowired to inject the share instance into
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String password) {
        System.out.println("Login with national ID: " + username);
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new AppException(MESSAGE));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(MESSAGE);
        }

        return user;
    }

    @Override
    public boolean checkType(String type) {
        return type.equals(this.type);
    }
}
