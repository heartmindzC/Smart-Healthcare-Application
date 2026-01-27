package com.example.hospitalservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "departments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    @UuidGenerator
    private String departmentId;
    private String departmentName;
    private String departmentPhone;
    private String departmentEmail;
    private String hospitalId;
}
