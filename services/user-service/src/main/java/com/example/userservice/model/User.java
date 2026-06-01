package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String userId;
    private String password;
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
