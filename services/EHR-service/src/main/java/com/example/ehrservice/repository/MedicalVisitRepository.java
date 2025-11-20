package com.example.ehrservice.repository;

import com.example.ehrservice.model.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {
    List<MedicalVisit> findByPatientId(Integer patientId);
    List<MedicalVisit> findByPatientIdOrderByVisitDateDesc(Integer patientId);
}


