package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    private String userId;
    private String password, phone, email, fullname, address;
    private Date birth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
