package com.example.doctorservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doctorId;
    private String userId;
    private int hospitalId;
    private String department;
    private String fullName; // ho ten
    private Date birth;
    private Date registrationAt;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String licenseId; //chung chi hanh nghe
    private Boolean isActive;

}
