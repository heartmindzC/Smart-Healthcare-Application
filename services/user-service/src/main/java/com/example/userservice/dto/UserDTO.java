package com.example.userservice.dto;

import com.example.userservice.model.Gender;
import com.example.userservice.model.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String userId;
    private String phone;
    private String email;
    private String fullname;
    private String address;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date birth;
    
    private Gender gender;
    private Set<Role> roles;
}

