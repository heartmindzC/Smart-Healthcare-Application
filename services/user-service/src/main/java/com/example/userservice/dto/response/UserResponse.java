package com.example.userservice.dto.response;

import com.example.userservice.model.Gender;
import com.example.userservice.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserResponse {
    @Id
    private String userId;
    private String phone;
    private String fullname;
    private String address;
    private Date birth;
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "role")
    private Set<Role> roles;
}
