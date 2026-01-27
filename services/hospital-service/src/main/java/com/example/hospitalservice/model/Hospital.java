package com.example.hospitalservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "hospitals")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {
    @Id
    @UuidGenerator
    private String hospitalId;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalPhone;
    private String hospitalEmail;
}
