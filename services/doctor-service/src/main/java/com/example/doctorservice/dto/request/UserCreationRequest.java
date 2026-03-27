package com.example.doctorservice.dto.request;

import com.example.doctorservice.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

/**
 * DTO dùng để gọi API POST /users/register của user-service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    private String userId;
    private String password;
    private String phone;
    private String email;
    private String fullname;
    private String address;
    private Date birth;
    private Gender gender;
    private Set<String> roles;
}
