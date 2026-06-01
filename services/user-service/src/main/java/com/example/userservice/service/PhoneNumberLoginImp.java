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
public class PhoneNumberLoginImp implements LoginMethod {
    private final String type = "phone";
    private final ErrorCode MESSAGE = UserErrorCode.LOGIN_FAILED;

     //Use @Autowired to inject the share instance into
    @Autowired
    private UserRepository userRepository;

    //User @Autowired to inject the share instance into
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String password) {
        System.out.println("Login with phone: " + username);
        User user = userRepository.findByPhone(username)
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
