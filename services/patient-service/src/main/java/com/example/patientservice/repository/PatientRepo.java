package com.example.patientservice.repository;

import com.example.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PatientRepo extends JpaRepository<Patient,String> {
    Optional<Patient> findByPatientId(String patientId);
    Optional<List<Patient>> findByFullName(String fullName);
    Optional<Patient> findByUserId(String userId);
}
