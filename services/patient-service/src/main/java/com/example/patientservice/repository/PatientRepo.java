package com.example.patientservice.repository;

import com.example.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepo extends JpaRepository<Patient,Integer> {
    Patient findByPatientId(Integer patientId);
    List<Patient> findByFullName(String fullName);
    Patient findByUserId(String userId);
}
