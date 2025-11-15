package com.example.userservice.dto;

import com.example.userservice.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String userId;
    private String password;
    private String phone;
    private String email;
    private String fullname;
    private String address;
    private Date birth;
    private Gender gender;
}

