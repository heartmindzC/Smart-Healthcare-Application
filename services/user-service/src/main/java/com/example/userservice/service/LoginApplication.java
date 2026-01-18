package com.example.userservice.service;

import com.example.userservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class LoginApplication {
    private final LoginMethod loginMethod;

    public LoginApplication(LoginMethod loginMethod) {
        this.loginMethod = loginMethod;
    }

    public User login(String username, String password) {
        return this.loginMethod.login(username, password);
    }
}
