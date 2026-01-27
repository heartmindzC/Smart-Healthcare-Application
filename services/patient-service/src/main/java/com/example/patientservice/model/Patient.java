package com.example.patientservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @UuidGenerator
    private String patientId;
    private String fullName;
    private String userId;
    private Date birth;
    private Date registeredAt;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String insuranceId;
    private String emergencyCallingNumber;
    private String job;
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;
    private Double heights;
    private Double weights;
    private Boolean isActive;
}


