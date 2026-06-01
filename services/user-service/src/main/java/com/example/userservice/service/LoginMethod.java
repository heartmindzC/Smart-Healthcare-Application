package com.example.userservice.service;

import com.example.userservice.model.User;

public interface LoginMethod {
    User login(String username, String password);
    boolean checkType(String type);
}
