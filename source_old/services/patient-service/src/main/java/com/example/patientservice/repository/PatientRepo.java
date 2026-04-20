package com.example.patientservice.repository;

import com.example.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PatientRepo extends JpaRepository<Patient,Integer> {
    Optional<Patient> findByPatientId(Integer patientId);
    Optional<List<Patient>> findByFullName(String fullName);
    Optional<Patient> findByUserId(Integer userId);
    Patient findByPatientId(Integer patientId);
    List<Patient> findByFullName(String fullName);
    Patient findByUserId(String userId);
}
