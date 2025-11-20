package com.example.ehrservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "prescriptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;
    
    @Column(nullable = false)
    private Long visitId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitId", insertable = false, updatable = false)
    private MedicalVisit visit;
    
    @Column(nullable = false)
    private Integer patientId;
    
    @Column(nullable = false)
    private String medicationName;
    
    @Column(nullable = false)
    private String dosage;
    
    @Column(nullable = false)
    private String frequency;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(columnDefinition = "TEXT")
    private String instructions;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date prescribedDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @PrePersist
    protected void onCreate() {
        if (prescribedDate == null) {
            prescribedDate = new Date();
        }
        createdAt = new Date();
    }
}


