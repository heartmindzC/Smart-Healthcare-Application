package com.example.ehrservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "medical_visits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long visitId;
    
    @Column(nullable = false)
    private Integer patientId;
    
    @Column(nullable = false)
    private String doctorId;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitDate;
    
    @Column(nullable = false)
    private String hospital;
    
    @Column(nullable = false)
    private String department;
    
    @Column(columnDefinition = "TEXT")
    private String diagnosis;
    
    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescriptions;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}


