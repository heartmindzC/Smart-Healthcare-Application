package com.example.patientservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer patientId;

    private String fullName;

    private String userId;

    private Date birth;
    
    private Date registeredAt;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String insuranceId, emergencyCallingNumber, job;

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    private Double heights, weights;

    private Boolean isActive;
}


