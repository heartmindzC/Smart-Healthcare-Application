package com.example.hospitalservice.repository;

import com.example.hospitalservice.model.Hospital;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends CrudRepository<Hospital, Integer> {
    List<Hospital> findHospitalByHospitalName(String hospitalName);
    List<Hospital> findHospitalByHospitalNameIgnoreCase(String hospitalName);
    List<Hospital> findHospitalByHospitalNameContainingIgnoreCase(String hospitalName);
    Optional<Hospital> findByHospitalId(int hospitalId);
    List<Hospital> findHospitalByHospitalAddressContaining(String address);
}

