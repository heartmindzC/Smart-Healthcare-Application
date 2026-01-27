package com.example.doctorservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @UuidGenerator
    private String doctorId;
    private String userId;
    private String hospitalId;
    private String department;
    private String fullName; // ho ten
    private Date birth;
    private Date registrationAt; // --
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String licenseId;
    private Boolean isActive; // --

}
