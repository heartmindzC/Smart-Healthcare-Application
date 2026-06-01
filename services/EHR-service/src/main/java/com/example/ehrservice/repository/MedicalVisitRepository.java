package com.example.ehrservice.repository;

import com.example.ehrservice.model.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, String> {
    List<MedicalVisit> findByPatientId(String patientId);
    List<MedicalVisit> findByPatientIdOrderByVisitDateDesc(String patientId);
}


