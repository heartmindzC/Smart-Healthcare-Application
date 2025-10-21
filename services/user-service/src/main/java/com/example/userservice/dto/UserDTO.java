package com.example.userservice.dto;

import com.example.userservice.model.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String phone, email, fullname, address;
    private Date birth;
    private Gender gender;
}
