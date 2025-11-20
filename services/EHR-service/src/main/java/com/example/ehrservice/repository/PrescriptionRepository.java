package com.example.ehrservice.repository;

import com.example.ehrservice.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByVisitId(Long visitId);
    List<Prescription> findByPatientId(Integer patientId);
    List<Prescription> findByPatientIdOrderByPrescribedDateDesc(Integer patientId);
    
    @Query("SELECT p FROM Prescription p WHERE p.visitId IN (SELECT v.visitId FROM MedicalVisit v WHERE v.patientId = :patientId)")
    List<Prescription> findAllByPatientId(@Param("patientId") Integer patientId);
}


